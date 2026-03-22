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

    @Column(name = "is_visa_enabled")
    private Boolean isVisaEnabled;

    @Column(name = "is_mastercard_enabled")
    private Boolean isMastercardEnabled;

    @Column(name = "is_tng_enabled")
    private Boolean isTngEnabled;

    @Column(name = "is_duitnow_enabled")
    private Boolean isDuitnowEnabled;

    @Column(name = "is_boost_enabled")
    private Boolean isBoostEnabled;

    @Column(name = "is_grab_enabled")
    private Boolean isGrabEnabled;



}