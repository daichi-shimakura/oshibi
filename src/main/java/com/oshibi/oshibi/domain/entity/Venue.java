package com.oshibi.oshibi.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "venues")
@Getter
@Setter
@NoArgsConstructor
public class Venue extends BaseEntity {

    @Id
    @Column(name = "venue_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long venueId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 20)
    private String prefecture;

    @Column(length = 500)
    private String address;

    @Column(name = "nearest_station", length = 200)
    private String nearestStation;

    @Column(name = "google_maps_url", length = 500)
    private String googleMapsUrl;
}