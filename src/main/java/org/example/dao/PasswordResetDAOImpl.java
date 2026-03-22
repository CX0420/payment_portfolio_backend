package org.example.dao;

import org.example.model.PasswordReset;
import org.example.util.DataConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class PasswordResetDAOImpl implements PasswordResetDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<PasswordReset> passwordResetRowMapper = new RowMapper<PasswordReset>() {
        @Override
        public PasswordReset mapRow(ResultSet rs, int rowNum) throws SQLException {
            PasswordReset reset = new PasswordReset();
            reset.setId(rs.getLong("id"));
            reset.setPin(rs.getString("pin"));
            reset.setMobileUserId(rs.getString("mobile_user_id"));
            reset.setStatus(rs.getString("status"));
            reset.setExpiresAt(DataConverterUtil.getLocalDateTime(rs, "expires_at"));
            reset.setUsedAt(DataConverterUtil.getLocalDateTime(rs, "used_at"));
            reset.setCreatedDateTime(DataConverterUtil.getLocalDateTime(rs, "created_datetime"));
            reset.setModifiedDateTime(DataConverterUtil.getLocalDateTime(rs, "modified_datetime"));
            return reset;
        }
    };

    @Override
    public void create(PasswordReset reset) {
        String sql = """
            INSERT INTO password_reset 
            (pin, mobile_user_fk, mobile_user_id, status, expires_at, used_at)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        jdbcTemplate.update(sql,
                reset.getPin(),
                reset.getMobileUser() != null ? reset.getMobileUser().getId() : null,
                reset.getMobileUserId(),
                reset.getStatus(),
                reset.getExpiresAt(),
                reset.getUsedAt()
        );
    }

    @Override
    public PasswordReset findActiveByMobileUserId(String mobileUserId) {
        String sql = """
            SELECT * FROM password_reset 
            WHERE mobile_user_id = ? 
            AND status = 'ACTIVE' 
            AND used_at IS NULL
            ORDER BY created_datetime DESC 
            LIMIT 1
            """;

        try {
            return jdbcTemplate.queryForObject(sql, passwordResetRowMapper, mobileUserId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void update(PasswordReset reset) {
        String sql = """
            UPDATE password_reset SET 
                status = ?,
                used_at = ?,
                modified_datetime = NOW()
            WHERE id = ?
            """;

        jdbcTemplate.update(sql,
                reset.getStatus(),
                reset.getUsedAt(),
                reset.getId()
        );
    }
}