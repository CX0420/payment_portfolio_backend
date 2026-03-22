package org.example.dao;

import org.example.dto.UniversalDTO;
import org.example.enumConstant.TransactionStatus;
import org.example.model.TransactionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TransactionDataDAOImpl implements TransactionDataDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public TransactionData create(TransactionData transaction) {
        String sql = """
            INSERT INTO transaction_data 
            (amount_authorized, pan, expiry_date, card_holder_name, status,
             card_type, mobile_user_id, mid, tid, batch_fk, mobile_user_fk,
             created_datetime, modified_datetime, created_by, modified_by)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW(), ?, ?)
            """;

        jdbcTemplate.update(sql,
                transaction.getAmountAuthorized(),
                transaction.getPan(),
                transaction.getExpiryDate(),
                transaction.getCardHolderName(),
                transaction.getStatus(),
                transaction.getCardType(),
                transaction.getMobileUserId(),
                transaction.getMid(),
                transaction.getTid(),
                transaction.getBatch() != null ? transaction.getBatch().getId() : null,
                transaction.getMobileUser() != null ? transaction.getMobileUser().getId() : null,
                transaction.getCreatedBy(),
                transaction.getModifiedBy()
        );

        // Get the last inserted ID
        String getIdSql = "SELECT lastval()";
        Long id = jdbcTemplate.queryForObject(getIdSql, Long.class);
        transaction.setId(id);

        return transaction;
    }

    @Override
    public List<TransactionData> findByMobileUserId(String mobileUserId) {
        String sql = """
                select id, amount_authorized, pan, expiry_date, card_holder_name,
                       status, settlement_date, voided_date, card_type, mobile_user_id,
                       mid, tid, created_datetime, modified_datetime, created_by, modified_by
                from transaction_data
                where mobile_user_id = ?
                order by created_datetime desc
                """;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TransactionData.class), mobileUserId);
    }

    @Override
    public List<TransactionData> getSalesHistoryListByBatch(UniversalDTO dto) {
        String sql = """
                select id, amount_authorized, pan, expiry_date, card_holder_name,
                       status, settlement_date, voided_date, card_type, mobile_user_id,
                       mid, tid, created_datetime, modified_datetime, created_by, modified_by
                from transaction_data
                where batch_fk = ?
                order by created_datetime desc
                """;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TransactionData.class), dto.getBatch().getId());
    }

    @Override
    public List<TransactionData> getUnsettledTransactionsList(UniversalDTO dto) {
        String sql = """
                select id, amount_authorized, pan, expiry_date, card_holder_name,
                       status, settlement_date, voided_date, card_type, mobile_user_id,
                       mid, tid, created_datetime, modified_datetime, created_by, modified_by
                from transaction_data
                where status != ? AND settlement_date is null and mobile_user_id = ?
                order by created_datetime desc
                """;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TransactionData.class), TransactionStatus.SETTLED.getCodeInStringFormat(), dto.getMobileUserId());
    }

    public TransactionData getSalesHistoryDetailById(Long id) {
        String sql = """
        SELECT id, amount_authorized, pan, expiry_date, card_holder_name,
               status, settlement_date, voided_date, card_type, mobile_user_id,
               mid, tid, created_datetime, modified_datetime, created_by, modified_by
        FROM transaction_data
        WHERE id = ?
        """;

        List<TransactionData> results = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(TransactionData.class),
                id
        );

        if (results.isEmpty()) {
            throw new RuntimeException("Transaction not found with ID: " + id);
        }

        return results.get(0);
    }

    @Override
    public void voidTransaction(UniversalDTO dto) {
        String sql = """
                update transaction_data
                set status = ?, voided_date = now(), modified_datetime = now(), modified_by = ?
                where id = ?
                """;

        jdbcTemplate.update(sql,
                TransactionStatus.VOID.getCode(),
                "SYSTEM",
                dto.getVoidTrxId());
    }

    @Override
    public void doSettlement(List<TransactionData> transactionDataList) {
        if (transactionDataList == null || transactionDataList.isEmpty()) {
            return;
        }

        // Separate transactions by status
        List<Long> status100Ids = transactionDataList.stream()
                .filter(t -> TransactionStatus.APPROVED.getCodeInStringFormat().equals(t.getStatus()))
                .map(TransactionData::getId)
                .collect(Collectors.toList());

        List<Long> otherStatusIds = transactionDataList.stream()
                .filter(t -> !TransactionStatus.APPROVED.getCodeInStringFormat().equals(t.getStatus()))
                .map(TransactionData::getId)
                .collect(Collectors.toList());

        // Update status 100 transactions (status + settlement date)
        if (!status100Ids.isEmpty()) {
            String placeholders = status100Ids.stream()
                    .map(id -> "?")
                    .collect(Collectors.joining(","));

            String sql = String.format("""
            UPDATE transaction_data
            SET status = ?, settlement_date = NOW(), modified_datetime = NOW(), modified_by = ?
            WHERE id IN (%s)
            """, placeholders);

            List<Object> params = new ArrayList<>();
            params.add(TransactionStatus.SETTLED.getCode());
            params.add("SYSTEM");
            params.addAll(status100Ids);

            jdbcTemplate.update(sql, params.toArray());
        }

        // Update other status transactions (settlement date only)
        if (!otherStatusIds.isEmpty()) {
            String placeholders = otherStatusIds.stream()
                    .map(id -> "?")
                    .collect(Collectors.joining(","));

            String sql = String.format("""
            UPDATE transaction_data
            SET settlement_date = NOW(), modified_datetime = NOW(), modified_by = ?
            WHERE id IN (%s)
            """, placeholders);

            List<Object> params = new ArrayList<>();
            params.add("SYSTEM");
            params.addAll(otherStatusIds);

            jdbcTemplate.update(sql, params.toArray());
        }
    }

    @Override
    public TransactionData update(TransactionData transactionData) {
        String sql = """
                update transaction_data
                set amount_authorized = ?, pan = ?, expiry_date = ?, card_holder_name = ?,
                    status = ?, card_type = ?, mobile_user_id = ?, mid = ?, tid = ?,
                    settlement_date = ?, voided_date = ?, modified_datetime = now(), modified_by = ?
                where id = ?
                """;

        jdbcTemplate.update(sql,
                transactionData.getAmountAuthorized(),
                transactionData.getPan(),
                transactionData.getExpiryDate(),
                transactionData.getCardHolderName(),
                transactionData.getStatus(),
                transactionData.getCardType(),
                transactionData.getMobileUserId(),
                transactionData.getMid(),
                transactionData.getTid(),
                transactionData.getSettlementDate(),
                transactionData.getVoidedDate(),
                "SYSTEM",
                transactionData.getId());

        return transactionData;
    }

    @Override
    public boolean delete(Long id) {
        String sql = "delete from transaction_data where id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    @Override
    public TransactionData findById(Long id) {
        String sql = """
        SELECT id, amount_authorized, pan, expiry_date, card_holder_name,
               status, settlement_date, voided_date, card_type, mobile_user_id,
               mid, tid, created_datetime, modified_datetime, created_by, modified_by
        FROM transaction_data
        WHERE id = ?
        """;

        List<TransactionData> results = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(TransactionData.class),
                id
        );

        if (results.isEmpty()) {
            throw new RuntimeException("Transaction not found with ID: " + id);
        }

        return results.get(0);
    }
}
