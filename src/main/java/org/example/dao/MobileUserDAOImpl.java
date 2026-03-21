package org.example.dao;

import org.example.model.MobileUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MobileUserDAOImpl implements MobileUserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<MobileUser> getMobileUserList() {
        String sql = """
                select
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
                from mobile_user
                order by id desc
                """;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(MobileUser.class));
    }

    @Override
    public boolean existsByMobileUserId(String mobileUserId) {
        String sql = "SELECT COUNT(*) FROM mobile_user WHERE mobile_user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, mobileUserId);
        return count != null && count > 0;
    }

    @Override
    public void create(MobileUser mobileUser) {
        String sql = """
                INSERT INTO mobile_user (mobile_user_id, mobile_user_name, pin, email, activated_date, wrong_password_count, status, created_datetime, modified_datetime)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(sql,
                mobileUser.getMobileUserId(),
                mobileUser.getMobileUserName(),
                mobileUser.getPin(),
                mobileUser.getEmail(),
                mobileUser.getActivatedDate(),
                mobileUser.getWrongPasswordCount(),
                mobileUser.getStatus(),
                mobileUser.getCreatedDatetime(),
                mobileUser.getModifiedDateTime());
    }

    @Override
    public MobileUser findByMobileUserId(String mobileUserId) {
        String sql = """
                SELECT id, mobile_user_id, mobile_user_name, pin, email, activated_date, wrong_password_count, status, created_datetime, modified_datetime
                FROM mobile_user WHERE mobile_user_id = ?
                """;
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(MobileUser.class), mobileUserId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public MobileUser findById(Long id) {
        String sql = """
                SELECT id, mobile_user_id, mobile_user_name, pin, email, activated_date, wrong_password_count, status, created_datetime, modified_datetime
                FROM mobile_user WHERE id = ?
                """;
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(MobileUser.class), id);
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
                    modified_datetime = ?
                WHERE id = ?
                """;
        jdbcTemplate.update(sql,
                mobileUser.getMobileUserName(),
                mobileUser.getPin(),
                mobileUser.getEmail(),
                mobileUser.getActivatedDate(),
                mobileUser.getWrongPasswordCount(),
                mobileUser.getStatus(),
                mobileUser.getModifiedDateTime(),
                mobileUser.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM mobile_user WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
