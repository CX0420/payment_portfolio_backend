package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TransactionData extends BasicTable {

    @Column(name = "amount_authorized")
    private Long amountAuthorized;

    @Column(name = "pan", length = 19)
    private String pan;

    @Column(name = "expiry_date", length = 5)
    private String expiryDate;

    @Column(name = "card_holder_name", length = 100)
    private String cardHolderName;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "settlement_date")
    private LocalDateTime settlementDate;

    @Column(name = "voided_date")
    private LocalDateTime voidedDate;

    @Column(name = "card_type", length = 50)
    private String cardType;

    @Column(name = "mobile_user_id", length = 50)
    private String mobileUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mobile_user_fk")
    private MobileUser mobileUser;

    @Column(name = "mid", length = 50)
    private String mid;

    @Column(name = "tid", length = 50)
    private String tid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_fk")
    private Batch batch;

    @Column(name = "host_response_code")
    private String hostResponseCode;
}