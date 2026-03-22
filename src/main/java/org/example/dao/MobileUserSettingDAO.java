package org.example.dao;

import org.example.model.MobileUserSetting;

import java.util.List;
import java.util.Optional;

public interface MobileUserSettingDAO {
    public List<MobileUserSetting> getMobileUserSettingList();

    // Add missing methods
    MobileUserSetting findByMobileUserId(String mobileUserId);
    Optional<MobileUserSetting> findById(Long id);
    MobileUserSetting create(MobileUserSetting setting);
    void update(MobileUserSetting setting);
    void delete(Long id);
}
