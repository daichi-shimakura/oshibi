package com.oshibi.oshibi.repository;

import com.oshibi.oshibi.domain.entity.Live;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LiveRepository extends JpaRepository<Live, Long> {
    List<Live> findByCreatedBy_AccountIdAndDateGreaterThanEqual(Long accountId, LocalDate liveDate);
}