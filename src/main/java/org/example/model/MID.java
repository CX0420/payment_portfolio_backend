package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "mid")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MID extends BasicTable {

    @Column(name = "mid", length = 50)
    private String mid;

    @Column(name = "card_type", length = 50)
    private String cardType;
}