package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "mobile_user_status_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MobileUserStatusHistory extends BasicTable {

    @Column(name = "history_remark", length = 500)
    private String historyRemark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mobile_user_fk")
    private MobileUser mobileUser;
}