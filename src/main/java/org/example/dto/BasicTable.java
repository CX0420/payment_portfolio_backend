package org.example.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BasicTable {
    private long id;
    private LocalDateTime createdDatetime;
    private LocalDateTime modifiedDatetime;
}