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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lives")
@Getter
@Setter
@NoArgsConstructor
public class Live extends BaseEntity {
    @Id
    @Column(name = "live_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long liveId;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private Account createdBy;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "live_type", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private LiveType liveType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "price_advance")
    private Integer priceAdvance;

    @Column(name = "price_door")
    private Integer priceDoor;

    @Column(name = "ticket_method", length = 30)
    @Enumerated(EnumType.STRING)
    private TicketMethod ticketMethod;

    @Column(name = "has_streaming", nullable = false)
    private Boolean hasStreaming;

    @Column(name = "streaming_price")
    private Integer streamingPrice;

    @Column(name = "streaming_start_date")
    private LocalDate streamingStartDate;

    @Column(name = "streaming_end_date")
    private LocalDate streamingEndDate;

    @Column(name = "flyer_url", length = 500)
    private String flyerUrl;

    @OneToMany(mappedBy = "live")
    private List<LivePerformer> livePerformers = new ArrayList<>();
}