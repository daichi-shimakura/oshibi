package com.oshibi.oshibi.service;

import com.oshibi.oshibi.dto.LiveListItemDto;
import com.oshibi.oshibi.repository.LivePerformerRepository;
import com.oshibi.oshibi.repository.LiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LiveService {
    private final LiveRepository liveRepository;

    public List<LiveListItemDto> findAll() {
        return liveRepository.findAll().stream()
                .map(live -> new LiveListItemDto(
                        live.getLiveId(),
                        live.getTitle(),
                        live.getDate(),
                        live.getOpenTime(),
                        live.getStartTime(),
                        live.getVenue().getName(),
                        live.getVenue().getPrefecture(),
                        live.getLiveType(),
                        live.getPriceAdvance(),
                        live.getPriceDoor(),
                        List.of(),  // comedianNames（仮置き）
                        0           // otherPerformerCount（仮置き）
                )).toList();
    }
}
