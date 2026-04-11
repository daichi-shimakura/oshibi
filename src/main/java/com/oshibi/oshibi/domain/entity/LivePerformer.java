package com.oshibi.oshibi.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "live_performers")
@Getter
@Setter
@NoArgsConstructor
public class LivePerformer extends BaseEntity {
    @Id
    @Column(name = "live_performer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long livePerformerId;

    @ManyToOne
    @JoinColumn(name = "live_id", nullable = false)
    private Live live;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private ComedianProfile comedian;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private PerformerStatus status;

    @Column(name = "pre_comment", columnDefinition = "TEXT")
    private String preComment;

    @Column(name = "neta_count")
    private Integer netaCount;

    @Column(name = "neta_type", length = 200)
    private String netaType;

    @Column(name = "guest_name", length = 100)
    private String guestName;
}