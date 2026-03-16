package com.oshibi.oshibi.service;

import com.oshibi.oshibi.dto.*;
import com.oshibi.oshibi.repository.ComedianProfileRepository;
import com.oshibi.oshibi.repository.LivePerformerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComedianService {
    private final ComedianProfileRepository comedianProfileRepository;
    private final LivePerformerRepository livePerformerRepository;

    public List<ComedianListItemDto> findAll() {
        return comedianProfileRepository.findAll().stream()
                .map(cp -> new ComedianListItemDto(
                        cp.getAccountId(),
                        cp.getAccount().getDisplayName(),
                        cp.getUnitType(),
                        cp.getAgency()
                )).toList();
    }

    public Optional<ComedianDetailDto> findById(Long accountId) {

        return comedianProfileRepository.findById(accountId).map(comedian -> {
            // comedianが出演するライブのリストを取得
            List<ComedianScheduleItemDto> lives = livePerformerRepository.findByComedian_AccountId(accountId).stream()
                    .map(lp -> new ComedianScheduleItemDto(
                            lp.getLive().getLiveId(),
                            lp.getLive().getTitle(),
                            lp.getLive().getDate(),
                            lp.getLive().getOpenTime(),
                            lp.getLive().getStartTime(),
                            lp.getLive().getVenue().getName(),
                            lp.getLive().getVenue().getPrefecture(),
                            lp.getLive().getLiveType(),
                            lp.getLive().getPriceAdvance(),
                            lp.getLive().getPriceDoor()
                    )).toList();


            // 作ったlivesを渡す
            return new ComedianDetailDto(
                    comedian.getAccount().getDisplayName(),
                    comedian.getUnitType(),
                    comedian.getAgency(),
                    comedian.getMemberNames(),
                    comedian.getProfileImageUrl(),
                    comedian.getAccountId(),
                    comedian.getAccount().getDescription(),
                    comedian.getAccount().getXUrl(),
                    comedian.getAccount().getInstagramUrl(),
                    comedian.getAccount().getYoutubeUrl(),
                    comedian.getAccount().getTiktokUrl(),
                    comedian.getAccount().getNoteUrl(),
                    comedian.getAccount().getPodcastUrl(),
                    lives
            );
        });
    }
}
