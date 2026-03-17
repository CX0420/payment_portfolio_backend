package org.example.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MobileUser extends BasicTable{
    private String mobileUserId;
    private String mobileUserName;
    private String pin;
    private String email;
    private LocalDateTime activatedDate;
    private int wrongPasswordCount;
}

