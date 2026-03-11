package com.oshibi.oshibi.repository;

import com.oshibi.oshibi.domain.entity.LivePerformer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivePerformerRepository extends JpaRepository<LivePerformer, Long> {
    List<LivePerformer> findByComedian_AccountId(Long accountId);// コメディアンのアカウントIDから、そのコメディアンが出演するライブのリストを取得
}


