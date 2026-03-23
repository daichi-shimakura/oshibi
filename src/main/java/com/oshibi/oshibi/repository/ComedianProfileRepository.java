package com.oshibi.oshibi.repository;

import com.oshibi.oshibi.domain.entity.ComedianProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ComedianProfileRepository extends JpaRepository<ComedianProfile, Long>, JpaSpecificationExecutor<ComedianProfile>  {
}
