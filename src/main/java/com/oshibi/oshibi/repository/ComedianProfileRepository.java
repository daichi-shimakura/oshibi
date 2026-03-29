package com.oshibi.oshibi.repository;

import com.oshibi.oshibi.domain.entity.ComedianProfile;
import com.oshibi.oshibi.dto.ComedianSearchResultDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface ComedianProfileRepository extends JpaRepository<ComedianProfile, Long>, JpaSpecificationExecutor<ComedianProfile>  {
    List<ComedianProfile> findByAccount_DisplayNameContainingIgnoreCase(String accountDisplayName);
}
