package org.example.dao;

import org.example.dto.UniversalDTO;
import org.example.model.TransactionData;

import java.util.List;

public interface TransactionDataDAO {

    TransactionData create(TransactionData transactionData);

    List<TransactionData> findByMobileUserId(String mobileUserId);

    List<TransactionData> getSalesHistoryListByBatch(UniversalDTO dto);

    List<TransactionData> getUnsettledTransactionsList(UniversalDTO dto);

    TransactionData getSalesHistoryDetailById(Long id);
    TransactionData update(TransactionData transactionData);

    void voidTransaction(UniversalDTO dto);

    void doSettlement(List<TransactionData> transactionDataList);
    boolean delete(Long id);

    TransactionData findById(Long id);
}
