package com.oshibi.oshibi.service;

import com.oshibi.oshibi.dto.LiveDetailDto;
import com.oshibi.oshibi.dto.LiveListItemDto;
import com.oshibi.oshibi.dto.PerformerDto;
import com.oshibi.oshibi.repository.LivePerformerRepository;
import com.oshibi.oshibi.repository.LiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<LiveDetailDto> findById(Long liveId) {
        return liveRepository.findById(liveId).map(live -> new LiveDetailDto(
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
                live.getLivePerformers().stream().map(lp -> new PerformerDto(
                        lp.getComedian().getAccountId(),
                        lp.getComedian().getAccount().getDisplayName(),
                        lp.getDisplayOrder())).toList(),
                live.getTicketMethod(),
                live.getHasStreaming(),
                live.getStreamingPrice(),
                live.getStreamingStartDate(),
                live.getStreamingEndDate(),
                live.getFlyerUrl(),
                live.getCreatedBy().getAccountId(),
                live.getCreatedBy().getDisplayName(),
                live.getDescription(),
                live.getVenue().getAddress(),
                live.getVenue().getNearestStation(),
                live.getVenue().getGoogleMapsUrl()
                ));
    }
}
