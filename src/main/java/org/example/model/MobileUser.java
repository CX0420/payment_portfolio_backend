package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@Entity
@Table(name = "mobile_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MobileUser extends BasicTable {

    @Column(name = "mobile_user_id", length = 50)
    private String mobileUserId;

    @Column(name = "mobile_user_name", length = 100)
    private String mobileUserName;

    @Column(name = "pin", length = 255)
    private String pin;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "activated_date")
    private LocalDateTime activatedDate;

    @Column(name = "wrong_password_count")
    private Integer wrongPasswordCount = 0;

    @Column(name = "status", length = 50)
    private String status;

    @OneToOne
    @JoinColumn(name = "mobile_user_setting_fk")
    @JsonIgnoreProperties("mobileUser")
    private MobileUserSetting mobileUserSetting;
}