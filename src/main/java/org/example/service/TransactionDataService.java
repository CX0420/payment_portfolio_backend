package org.example.service;

import jakarta.annotation.Resource;
import org.example.dao.*;
import org.example.dto.ApiResponse;
import org.example.dto.PaymentDTO;
import org.example.dto.UniversalDTO;
import org.example.enumConstant.PaymentType;
import org.example.enumConstant.Status;
import org.example.enumConstant.TransactionStatus;
import org.example.model.*;
import org.springframework.stereotype.Service;

import host.HostSimulator;
import org.example.dto.Iso8583MessageDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TransactionDataService {

    @Resource
    private TransactionDataDAO transactionDataDAO;

    @Resource
    private BatchDAO batchDAO;

    @Resource
    private TIDDAO tidDAO;

    @Resource
    private MobileUserDAO mobileUserDAO;

    @Resource
    private MIDDAO midDAO;

    /**
     * Process transaction request using UniversalDTO
     */
    public ApiResponse<UniversalDTO> processTransaction(UniversalDTO dto) {
        try {

            if (dto.getMobileUserId() == null || dto.getAmountAuthorized() == null ||
                    dto.getCardData() == null || dto.getCardType() == null) {
                return ApiResponse.badRequest("Required transaction fields are missing");
            }

            List<TID> tid = tidDAO.findByMobileUserId(dto.getMobileUserId());
            Batch batch = batchDAO.findLatestByTidFk(tid.get(0).getId());
            MobileUser mobileUser = mobileUserDAO.findByMobileUserId(dto.getMobileUserId());
            MID mid = midDAO.findById(tid.get(0).getMidFk());

            // Check if batch is null OR if batch is SETTLED
            if (batch == null || "SETTLED".equals(batch.getStatus())) {
                Batch newBatch = new Batch();
                newBatch.setTid(tid.get(0));
                newBatch.setStatus("PENDING");
                newBatch.setCreatedBy(dto.getMobileUserId());
                batch = batchDAO.create(newBatch);
            }

            TransactionData td = new TransactionData();
            td.setMid(mid.getMid());
            td.setTid(tid.get(0).getTid());
            td.setPan(dto.getCardData());
            td.setCardType(dto.getCardType());
            td.setAmountAuthorized(dto.getAmountAuthorized());
            td.setStatus("PENDING");
            td.setMobileUserId(dto.getMobileUserId());
            td.setBatch(batch);
            td.setMobileUser(mobileUser);
            td.setCreatedBy("SYSTEM");

            transactionDataDAO.create(td);

            tid.get(0).setStan(tid.get(0).getStan()+1L);
            tid.get(0).setTrace(tid.get(0).getTrace()+1L);
            tidDAO.update(tid.get(0));

            // Send to host for processing
            Iso8583MessageDto isoDto = new Iso8583MessageDto(td, tid.get(0), "SALE");
            PaymentDTO payment = new PaymentDTO(td.getAmountAuthorized() / 100.0, td.getStatus(), PaymentType.CARD.name());
            payment.setTransactionType("SALE");

            HostSimulator hostSimulator = new HostSimulator();
            PaymentDTO updatedPayment = hostSimulator.processTransaction(isoDto, payment);

            // Update transaction status based on host response
            td.setStatus(Objects.equals(updatedPayment.getStatus(), "00") ? String.valueOf(TransactionStatus.APPROVED.getCode()) : "");
            td.setHostResponseCode(updatedPayment.getStatus());
            transactionDataDAO.update(td);

            return ApiResponse.success(dto, "Transaction processed successfully", 200);

        } catch (Exception e) {
            return ApiResponse.internalServerError("Transaction processing failed: " + e.getMessage());
        }
    }

    public TransactionData create(TransactionData transactionData) {
        return transactionDataDAO.create(transactionData);
    }

    public TransactionData findById(Long id) {
        return transactionDataDAO.findById(id);
    }

    public List<TransactionData> findByMobileUserId(String mobileUserId) {
        return transactionDataDAO.findByMobileUserId(mobileUserId);
    }

    public ApiResponse<UniversalDTO> getUnsettledTransactionsList (UniversalDTO dto) {
        if(dto.getMobileUserId() == null){
            return ApiResponse.notFound("No Mobile User ID");
        }
        List<TransactionData> transactionDataList = transactionDataDAO.getUnsettledTransactionsList(dto);
        dto.setTransactionDataList(transactionDataList);
        return ApiResponse.success(dto, "Get Transaction List successful");
    }

    public ApiResponse<UniversalDTO> getSalesHistoryDetailById (UniversalDTO dto) {
        if(dto.getSalesTrxId() == null){
            return ApiResponse.notFound("No SalesTrxID");
        }

        TransactionData transactionData = transactionDataDAO.getSalesHistoryDetailById(dto.getSalesTrxId());

        if(transactionData == null){
            return ApiResponse.notFound("Transaction not found");
        }

        List<TransactionData> transactionDataList = new ArrayList<>();
        transactionDataList.add(transactionData);
        dto.setTransactionDataList(transactionDataList);
        return ApiResponse.success(dto, "Get Transaction List successful");

    }

    public ApiResponse<UniversalDTO> voidTransaction (UniversalDTO dto) {

        if(dto.getMobileUserId() == null || dto.getVoidTrxId() == null){
            return ApiResponse.notFound("No VoidTrxID");
        }

        TransactionData voidTransaction = transactionDataDAO.getSalesHistoryDetailById(dto.getVoidTrxId());
        if(voidTransaction == null){
            return ApiResponse.notFound("Void Transaction not found");
        }

        if(Objects.equals(voidTransaction.getStatus(), Objects.toString(TransactionStatus.VOID.getCode()))) {
            return ApiResponse.badRequest("Void Transaction is already VOIDED");
        }
        else if(Objects.equals(voidTransaction.getStatus(), Objects.toString(TransactionStatus.PENDING.getCode()))) {
            return ApiResponse.badRequest("Transaction is NOT Approved");
        }
        else if(Objects.equals(voidTransaction.getStatus(), Objects.toString(TransactionStatus.SETTLED.getCode()))) {
            return ApiResponse.badRequest("Transaction is SETTLED");
        }

        List<TID> tid = tidDAO.findByMobileUserId(dto.getMobileUserId());

        // Send to host for processing
        Iso8583MessageDto isoDto = new Iso8583MessageDto(voidTransaction, tid.get(0), "SALE");
        PaymentDTO payment = new PaymentDTO(voidTransaction.getAmountAuthorized() / 100.0, voidTransaction.getStatus(), PaymentType.CARD.name());
        payment.setTransactionType("VOID");

        HostSimulator hostSimulator = new HostSimulator();
        PaymentDTO updatedPayment = hostSimulator.processTransaction(isoDto, payment);

        if(updatedPayment.getStatus().equals("00"))
        {
            transactionDataDAO.voidTransaction(dto);
        }

        return ApiResponse.success(dto, "Void Transaction successful");

    }

    public ApiResponse<UniversalDTO> doSettlement (UniversalDTO dto) {
        if(dto.getMobileUserId() == null){
            return ApiResponse.notFound("No Mobile User ID");
        }

        List<TID> tid = tidDAO.findByMobileUserId(dto.getMobileUserId());
        Batch batch = batchDAO.findLatestByTidFk(tid.get(0).getId());
        MobileUser mobileUser = mobileUserDAO.findByMobileUserId(dto.getMobileUserId());
        MID mid = midDAO.findById(tid.get(0).getMidFk());

        dto.setBatch(batch);

        if(batch == null && "SUCCESSFUL".equals(batch.getStatus())){
            return ApiResponse.notFound("No new transactions found");
        }

        List<TransactionData> transactionDataList = transactionDataDAO.getSalesHistoryListByBatch(dto);
        Long totalAuthorizedAmount = transactionDataList.stream()
                .filter(t -> "100".equals(t.getStatus()))  // Filter only status "100"
                .mapToLong(TransactionData::getAmountAuthorized)
                .sum();

        TransactionData td = new TransactionData();
        td.setAmountAuthorized(totalAuthorizedAmount);
        td.setTid(tid.get(0).getTid());
        td.setMid(mid.getMid());

        Iso8583MessageDto isoDto = new Iso8583MessageDto(td, tid.get(0), "SALE");
        PaymentDTO payment = new PaymentDTO(td.getAmountAuthorized() / 100.0, td.getStatus(), PaymentType.CARD.name());
        payment.setTransactionType("SETTLEMENT");

        HostSimulator hostSimulator = new HostSimulator();
        PaymentDTO updatedPayment = hostSimulator.processTransaction(isoDto, payment);

        if(updatedPayment.getStatus().equals("00"))
        {
            transactionDataDAO.doSettlement(transactionDataList);
            batchDAO.updateStatus(batch.getId(), "SUCCESSFUL", mobileUser.getMobileUserId());
        }

        return ApiResponse.success(dto, "Settlement successful");

    }

    public TransactionData update(TransactionData transactionData) {
        return transactionDataDAO.update(transactionData);
    }

    public boolean delete(Long id) {
        return transactionDataDAO.delete(id);
    }
}
