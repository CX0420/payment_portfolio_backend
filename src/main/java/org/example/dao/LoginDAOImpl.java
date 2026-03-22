package org.example.dao;

import org.example.model.MobileUser;
import org.example.model.MobileUserSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LoginDAOImpl implements LoginDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MobileUserSettingDAO mobileUserSettingDAO;

    @Override
    public MobileUser findByMobileUserId(String mobileUserId) {
        String sql = """
                select
                  id,
                  mobile_user_id,
                  mobile_user_name,
                  email,
                  activated_date,
                  wrong_password_count,
                  created_datetime,
                  modified_datetime
                from mobile_user
                where email = ? or mobile_user_id = ?
                limit 1
                """;

        List<MobileUser> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(MobileUser.class), mobileUserId,
                mobileUserId);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public String getPasswordByMobileUserId(String mobileUserId) {
        String sql = """
                select pin
                from mobile_user
                where email = ? or mobile_user_id = ?
                limit 1
                """;

        List<String> results = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("pin"), mobileUserId, mobileUserId);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public boolean existsByMobileUserId(String mobileUserId) {
        String sql = "select count(*) from mobile_user where mobile_user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, mobileUserId);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "select count(*) from mobile_user where email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public MobileUser create(MobileUser user) {
        // First create the setting
        MobileUserSetting setting = new MobileUserSetting();
        setting.setIsNotificationOn(true);
        setting.setIsDarkModeOn(false);
        setting.setSelectedLanguage("en");
        mobileUserSettingDAO.create(setting);

        // Get the created setting ID
        MobileUserSetting createdSetting = mobileUserSettingDAO.findById(setting.getId()).orElse(null);

        String sql = """
                insert into mobile_user
                (mobile_user_id, mobile_user_name, pin, email, activated_date,
                 wrong_password_count, status, created_datetime, modified_datetime,
                 created_by, modified_by, mobile_user_setting_fk)
                values (?, ?, ?, ?, now(), 0, 'ACTIVE', now(), now(), ?, ?, ?)
                """;

        jdbcTemplate.update(sql,
                user.getMobileUserId(),
                user.getMobileUserName(),
                user.getPin(),
                user.getEmail(),
                "SYSTEM",
                "SYSTEM",
                createdSetting != null ? createdSetting.getId() : null);

        return user;
    }

    @Override
    public void updatePassword(String mobileUserId, String hashedPassword) {
        String sql = """
                update mobile_user
                set pin = ?, wrong_password_count = 0, modified_datetime = now(), modified_by = ?
                where mobile_user_id = ?
                """;

        jdbcTemplate.update(sql, hashedPassword, "SYSTEM", mobileUserId);
    }

}
