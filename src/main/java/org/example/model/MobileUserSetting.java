package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "mobile_user_setting")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MobileUserSetting extends BasicTable {

    @Column(name = "is_notification_on")
    private Boolean isNotificationOn = true;

    @Column(name = "is_dark_mode_on")
    private Boolean isDarkModeOn = false;

    @Column(name = "selected_language", length = 10)
    private String selectedLanguage = "en";

    @OneToOne(mappedBy = "mobileUserSetting", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("mobileUserSetting")
    private MobileUser mobileUser;
}