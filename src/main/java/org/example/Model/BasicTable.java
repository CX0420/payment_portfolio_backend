package org.example.Model;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BasicTable {
    private long id;
    private LocalDateTime createdDatetime;
    private String createdBy;
    private LocalDateTime modifiedDateTime;
    private String modifiedBy;
}