package com.oshibi.oshibi.repository;

import com.oshibi.oshibi.domain.entity.Live;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiveRepository extends JpaRepository<Live, Long> {
}