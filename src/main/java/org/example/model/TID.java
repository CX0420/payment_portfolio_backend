package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tid")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TID extends BasicTable {

    @Column(name = "tid", length = 50)
    private String tid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mobile_user_fk")
    private MobileUser mobileUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mid_fk")
    private MID mid;

    @OneToMany(mappedBy = "tid", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("tid")
    private List<Batch> batches;
}