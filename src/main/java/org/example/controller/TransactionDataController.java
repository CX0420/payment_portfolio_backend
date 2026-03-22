package org.example.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.example.dto.ApiResponse;
import org.example.dto.Result;
import org.example.dto.TransactionDataRequest;
import org.example.dto.UniversalDTO;
import org.example.model.TransactionData;
import org.example.service.TransactionDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionDataController {

    @Resource
    private TransactionDataService transactionDataService;

    @PostMapping
    public Result<TransactionData> create(@Valid @RequestBody TransactionDataRequest request) {
        try {
            TransactionData transactionData = new TransactionData();
            transactionData.setAmountAuthorized(request.getAmountAuthorized());
            transactionData.setPan(request.getPan());
            transactionData.setExpiryDate(request.getExpiryDate());
            transactionData.setCardHolderName(request.getCardHolderName());
            transactionData.setStatus(request.getStatus());
            transactionData.setCardType(request.getCardType());
            transactionData.setMobileUserId(request.getMobileUserId());
            transactionData.setMid(request.getMid());
            transactionData.setTid(request.getTid());

            TransactionData created = transactionDataService.create(transactionData);
            return Result.success(created);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UniversalDTO>> getSalesHistoryDetailsById(@Valid @RequestBody UniversalDTO dto) {
        ;ApiResponse<UniversalDTO> response = (ApiResponse<UniversalDTO>) transactionDataService.getSalesHistoryDetailById(dto);
        if (response.getSuccess()) {
            if (response.getStatusCode() == 201) {
                return ResponseEntity.status(201).body(response);
            }
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<UniversalDTO>> getUnsettledTransactionsList(@Valid @RequestBody UniversalDTO dto) {
       ;ApiResponse<UniversalDTO> response = (ApiResponse<UniversalDTO>) transactionDataService.getUnsettledTransactionsList(dto);
        if (response.getSuccess()) {
            if (response.getStatusCode() == 201) {
                return ResponseEntity.status(201).body(response);
            }
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
    }

    @GetMapping("/void")
    public ResponseEntity<ApiResponse<UniversalDTO>> voidTransaction (@Valid @RequestBody UniversalDTO dto) {
        ;ApiResponse<UniversalDTO> response = (ApiResponse<UniversalDTO>) transactionDataService.voidTransaction(dto);
        if (response.getSuccess()) {
            if (response.getStatusCode() == 201) {
                return ResponseEntity.status(201).body(response);
            }
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
    }



    @PutMapping("/{id}")
    public Result<TransactionData> update(
            @PathVariable Long id,
            @Valid @RequestBody TransactionDataRequest request) {
        try {
            TransactionData existing = transactionDataService.findById(id);
            if (existing == null) {
                return new Result<>(404, "Transaction not found", null);
            }

            existing.setAmountAuthorized(request.getAmountAuthorized());
            existing.setPan(request.getPan());
            existing.setExpiryDate(request.getExpiryDate());
            existing.setCardHolderName(request.getCardHolderName());
            existing.setStatus(request.getStatus());
            existing.setCardType(request.getCardType());
            existing.setMobileUserId(request.getMobileUserId());
            existing.setMid(request.getMid());
            existing.setTid(request.getTid());

            TransactionData updated = transactionDataService.update(existing);
            return Result.success(updated);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            boolean deleted = transactionDataService.delete(id);
            if (!deleted) {
                return new Result<>(404, "Transaction not found", null);
            }
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
