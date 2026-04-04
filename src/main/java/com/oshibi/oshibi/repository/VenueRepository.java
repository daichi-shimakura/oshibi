package com.oshibi.oshibi.repository;

import com.oshibi.oshibi.domain.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findByNameContainingIgnoreCase(String keyword);
}
