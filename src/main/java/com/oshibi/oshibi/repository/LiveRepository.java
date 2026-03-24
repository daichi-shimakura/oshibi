package com.oshibi.oshibi.repository;

import com.oshibi.oshibi.domain.entity.Live;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import java.time.LocalDate;
import java.util.List;


public interface LiveRepository extends JpaRepository<Live, Long>, JpaSpecificationExecutor<Live> {

    List<Live> findByCreatedBy_AccountIdAndDateGreaterThanEqual(Long accountId, LocalDate liveDate);

    @EntityGraph(attributePaths = {"livePerformers"})
    List<Live> findAll(Specification<Live> spec);
}