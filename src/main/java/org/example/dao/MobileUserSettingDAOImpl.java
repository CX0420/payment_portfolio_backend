package org.example.dao;

import org.example.model.MobileUserSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MobileUserSettingDAOImpl implements MobileUserSettingDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<MobileUserSetting> getMobileUserSettingList() {
        String sql = """
                select id, is_notification_on, is_dark_mode_on, selected_language,
                       created_datetime, modified_datetime, created_by, modified_by
                from mobile_user_setting
                order by id desc
                """;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(MobileUserSetting.class));
    }

    @Override
    public MobileUserSetting findByMobileUserId(String mobileUserId) {
        String sql = """
                select ms.id, ms.is_notification_on, ms.is_dark_mode_on, ms.selected_language,
                       ms.created_datetime, ms.modified_datetime, ms.created_by, ms.modified_by
                from mobile_user_setting ms
                join mobile_user mu on ms.id = mu.mobile_user_setting_fk
                where mu.mobile_user_id = ?
                """;

        List<MobileUserSetting> results = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<>(MobileUserSetting.class), mobileUserId);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public Optional<MobileUserSetting> findById(Long id) {
        String sql = """
                select id, is_notification_on, is_dark_mode_on, selected_language,
                       created_datetime, modified_datetime, created_by, modified_by
                from mobile_user_setting
                where id = ?
                """;

        List<MobileUserSetting> results = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<>(MobileUserSetting.class), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public MobileUserSetting create(MobileUserSetting setting) {
        String sql = """
            INSERT INTO mobile_user_setting 
            (is_notification_on, is_dark_mode_on, selected_language, created_datetime, created_by)
            VALUES (?, ?, ?, NOW(), ?)
            RETURNING id
            """;

        Long id = jdbcTemplate.queryForObject(sql, Long.class,
                setting.getIsNotificationOn(),
                setting.getIsDarkModeOn(),
                setting.getSelectedLanguage(),
                setting.getCreatedBy() != null ? setting.getCreatedBy() : "SYSTEM"
        );

        setting.setId(id);
        return setting;
    }

    @Override
    public void update(MobileUserSetting setting) {
        String sql = """
                update mobile_user_setting set
                    is_notification_on = ?,
                    is_dark_mode_on = ?,
                    selected_language = ?,
                    modified_datetime = ?,
                    modified_by = ?
                where id = ?
                """;
        jdbcTemplate.update(sql,
            setting.getIsNotificationOn(),
            setting.getIsDarkModeOn(),
            setting.getSelectedLanguage(),
            setting.getModifiedDateTime(),
            setting.getModifiedBy(),
            setting.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from mobile_user_setting where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
