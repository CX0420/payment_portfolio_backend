package org.example.dao;

import org.example.model.Batch;
import org.example.model.TID;
import org.example.util.DataConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BatchDAOImpl implements BatchDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Batch> batchRowMapper = new RowMapper<Batch>() {
        @Override
        public Batch mapRow(ResultSet rs, int rowNum) throws SQLException {
            Batch batch = new Batch();
            batch.setId(rs.getLong("id"));
            batch.setStatus(rs.getString("status"));
            batch.setCreatedBy(rs.getString("created_by"));
            batch.setCreatedDateTime(DataConverterUtil.getLocalDateTime(rs, "created_datetime"));
            batch.setModifiedBy(rs.getString("modified_by"));
            batch.setModifiedDateTime(DataConverterUtil.getLocalDateTime(rs, "modified_datetime"));

            // Set TID reference
            if (rs.getObject("tid_fk") != null) {
                TID tid = new TID();
                tid.setId(rs.getLong("tid_fk"));
                batch.setTid(tid);
            }

            return batch;
        }
    };

    @Override
    public Batch create(Batch batch) {
        String sql = """
            INSERT INTO batch 
            (tid_fk, status, created_by, created_datetime, modified_datetime)
            VALUES (?, ?, ?, NOW(), NOW())
            RETURNING id
            """;

        Long id = jdbcTemplate.queryForObject(sql, Long.class,
                batch.getTid() != null ? batch.getTid().getId() : null,
                batch.getStatus(),
                batch.getCreatedBy()
        );

        batch.setId(id);
        return batch;
    }

    @Override
    public Batch findById(Long id) {
        String sql = """
            SELECT * FROM batch WHERE id = ?
            """;

        try {
            return jdbcTemplate.queryForObject(sql, batchRowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Batch> findAll() {
        String sql = """
            SELECT * FROM batch 
            ORDER BY created_datetime DESC
            """;

        return jdbcTemplate.query(sql, batchRowMapper);
    }

    @Override
    public List<Batch> findByTidFk(Long tidFk) {
        String sql = """
            SELECT * FROM batch 
            WHERE tid_fk = ? 
            ORDER BY created_datetime DESC
            """;

        return jdbcTemplate.query(sql, batchRowMapper, tidFk);
    }

    @Override
    public List<Batch> findActiveByTidFk(Long tidFk) {
        String sql = """
            SELECT * FROM batch 
            WHERE tid_fk = ? 
            AND status = 'ACTIVE'
            ORDER BY created_datetime DESC
            """;

        return jdbcTemplate.query(sql, batchRowMapper, tidFk);
    }

    @Override
    public List<Batch> findByTidFkAndStatus(Long tidFk, String status) {
        String sql = """
            SELECT * FROM batch 
            WHERE tid_fk = ? 
            AND status = ?
            ORDER BY created_datetime DESC
            """;

        return jdbcTemplate.query(sql, batchRowMapper, tidFk, status);
    }

    @Override
    public Batch findLatestByTidFk(Long tidFk) {
        String sql = """
            SELECT * FROM batch 
            WHERE tid_fk = ? 
            ORDER BY created_datetime DESC 
            LIMIT 1
            """;

        try {
            return jdbcTemplate.queryForObject(sql, batchRowMapper, tidFk);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Batch update(Batch batch) {
        String sql = """
            UPDATE batch SET 
                status = ?,
                modified_by = ?,
                modified_datetime = NOW()
            WHERE id = ?
            """;

        jdbcTemplate.update(sql,
                batch.getStatus(),
                batch.getModifiedBy(),
                batch.getId()
        );

        return batch;
    }

    @Override
    public boolean updateStatus(Long id, String status, String modifiedBy) {
        String sql = """
            UPDATE batch SET 
                status = ?,
                modified_by = ?,
                modified_datetime = NOW()
            WHERE id = ?
            """;

        int rows = jdbcTemplate.update(sql, status, modifiedBy, id);
        return rows > 0;
    }

    @Override
    public boolean updateStatusByTidFkAndBatchNumber(Long tidFk, String batchNumber, String status, String modifiedBy) {
        // Since your batch table doesn't have batch_number column,
        // you might need to use the ID instead
        // This method is kept for compatibility but you may want to remove it
        String sql = """
            UPDATE batch SET 
                status = ?,
                modified_by = ?,
                modified_datetime = NOW()
            WHERE tid_fk = ? AND id = ?
            """;

        // Assuming batchNumber is actually the ID
        try {
            Long batchId = Long.parseLong(batchNumber);
            int rows = jdbcTemplate.update(sql, status, modifiedBy, tidFk, batchId);
            return rows > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM batch WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return rows > 0;
    }

    @Override
    public int deleteByTidFk(Long tidFk) {
        String sql = "DELETE FROM batch WHERE tid_fk = ?";
        return jdbcTemplate.update(sql, tidFk);
    }

    @Override
    public int countByTidFk(Long tidFk) {
        String sql = "SELECT COUNT(*) FROM batch WHERE tid_fk = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tidFk);
        return count != null ? count : 0;
    }

    @Override
    public boolean existsByTidFkAndStatus(Long tidFk, String status) {
        String sql = "SELECT COUNT(*) FROM batch WHERE tid_fk = ? AND status = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tidFk, status);
        return count != null && count > 0;
    }
}