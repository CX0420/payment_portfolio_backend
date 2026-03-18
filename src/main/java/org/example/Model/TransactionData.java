package org.example.Model;

import java.time.LocalDateTime;

public class TransactionData extends BasicTable{
    private long amountAuthorized;
    private String pan;
    private String expiryDate;
    private String cardHolderName;
    private String status;
    private LocalDateTime settlementDate;
    private LocalDateTime voidedDate;
    private String cardType;
    private MobileUser mobileUser;
    private String mobileUserId;
    private String mid;
    private String tid;
}
