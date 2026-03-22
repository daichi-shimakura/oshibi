package com.oshibi.oshibi.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comedian_profiles")
@Getter
@Setter
@NoArgsConstructor
public class ComedianProfile extends BaseEntity {

    @Id
    @Column(name = "account_id")
    private Long accountId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "unit_type", nullable = false, length = 20)
    private String unitType;

    @Column(length = 100)
    private String agency;

    @Column(name = "member_names", columnDefinition = "TEXT")
    private String memberNames;
    
}