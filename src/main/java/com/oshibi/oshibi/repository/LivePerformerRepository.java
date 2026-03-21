package com.oshibi.oshibi.repository;

import com.oshibi.oshibi.domain.entity.LivePerformer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LivePerformerRepository extends JpaRepository<LivePerformer, Long> {

    List<LivePerformer> findByComedian_AccountId(Long accountId);// コメディアンのアカウントIDから、そのコメディアンが出演するライブのリストを取得

    Optional<LivePerformer> findByLive_LiveIdAndComedian_AccountId(Long liveId, Long accountId);

    List<LivePerformer> findByComedian_AccountIdAndLive_DateGreaterThanEqual(Long accountId, LocalDate liveDate);
}


