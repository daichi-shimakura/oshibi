package com.oshibi.oshibi.repository;

import com.oshibi.oshibi.domain.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Long> {
}
