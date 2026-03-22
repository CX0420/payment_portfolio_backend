package org.example.dao;

import org.example.model.MobileUser;
import org.example.util.DataConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MobileUserDAOImpl implements MobileUserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Custom RowMapper to handle timestamp conversion
    private RowMapper<MobileUser> mobileUserRowMapper = new RowMapper<MobileUser>() {
        @Override
        public MobileUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            MobileUser user = new MobileUser();
            user.setId(rs.getLong("id"));
            user.setMobileUserId(rs.getString("mobile_user_id"));
            user.setMobileUserName(rs.getString("mobile_user_name"));
            user.setPin(rs.getString("pin"));
            user.setEmail(rs.getString("email"));
            user.setActivatedDate(DataConverterUtil.getLocalDateTime(rs, "activated_date"));
            user.setWrongPasswordCount(rs.getInt("wrong_password_count"));
            user.setStatus(rs.getString("status"));
            user.setCreatedDateTime(DataConverterUtil.getLocalDateTime(rs, "created_datetime"));
            user.setModifiedDateTime(DataConverterUtil.getLocalDateTime(rs, "modified_datetime"));
            return user;
        }
    };

    @Override
    public List<MobileUser> getMobileUserList() {
        String sql = """
                SELECT
                  id,
                  mobile_user_id,
                  mobile_user_name,
                  pin,
                  email,
                  activated_date,
                  wrong_password_count,
                  status,
                  created_datetime,
                  modified_datetime
                FROM mobile_user
                ORDER BY id DESC
                """;

        return jdbcTemplate.query(sql, mobileUserRowMapper);
    }

    @Override
    public boolean existsByMobileUserId(String mobileUserId) {
        String sql = "SELECT COUNT(*) FROM mobile_user WHERE mobile_user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, mobileUserId);
        return count != null && count > 0;
    }

    @Override
    public MobileUser create(MobileUser user) {
        String sql = """
        INSERT INTO mobile_user 
        (mobile_user_id, mobile_user_name, pin, email, wrong_password_count, status, 
         mobile_user_setting_fk, created_by, created_datetime)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())
        RETURNING id
        """;

        Long id = jdbcTemplate.queryForObject(sql, Long.class,
                user.getMobileUserId(),
                user.getMobileUserName(),
                user.getPin(),
                user.getEmail(),
                user.getWrongPasswordCount(),
                user.getStatus(),
                user.getMobileUserSetting() != null ? user.getMobileUserSetting().getId() : null,
                user.getCreatedBy()
        );

        user.setId(id);
        return user;
    }

    @Override
    public MobileUser findByMobileUserId(String mobileUserId) {
        String sql = """
                SELECT id, mobile_user_id, mobile_user_name, pin, email, 
                       activated_date, wrong_password_count, status, 
                       created_datetime, modified_datetime
                FROM mobile_user WHERE mobile_user_id = ?
                """;
        try {
            return jdbcTemplate.queryForObject(sql, mobileUserRowMapper, mobileUserId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public MobileUser findById(Long id) {
        String sql = """
                SELECT id, mobile_user_id, mobile_user_name, pin, email, 
                       activated_date, wrong_password_count, status, 
                       created_datetime, modified_datetime
                FROM mobile_user WHERE id = ?
                """;
        try {
            return jdbcTemplate.queryForObject(sql, mobileUserRowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void update(MobileUser mobileUser) {
        String sql = """
                UPDATE mobile_user SET
                    mobile_user_name = ?,
                    pin = ?,
                    email = ?,
                    activated_date = ?,
                    wrong_password_count = ?,
                    status = ?,
                    modified_datetime = NOW()
                WHERE id = ?
                """;
        jdbcTemplate.update(sql,
                mobileUser.getMobileUserName(),
                mobileUser.getPin(),
                mobileUser.getEmail(),
                mobileUser.getActivatedDate(),
                mobileUser.getWrongPasswordCount(),
                mobileUser.getStatus(),
                mobileUser.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM mobile_user WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}