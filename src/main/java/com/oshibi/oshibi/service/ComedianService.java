package com.oshibi.oshibi.service;

import com.oshibi.oshibi.domain.entity.ComedianProfile;
import com.oshibi.oshibi.dto.*;
import com.oshibi.oshibi.repository.ComedianProfileRepository;
import com.oshibi.oshibi.repository.ComedianSpecification;
import com.oshibi.oshibi.repository.LivePerformerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComedianService {
    private final ComedianProfileRepository comedianProfileRepository;
    private final LivePerformerRepository livePerformerRepository;

    public Page<ComedianListItemDto> search(ComedianSearchDto dto, Pageable pageable) {

        Specification<ComedianProfile> spec = (root, query, cb) -> cb.conjunction();

        if (dto.getKeyWord() != null && !dto.getKeyWord().isBlank()) {
            spec = spec.and(ComedianSpecification.keywordSearch(dto.getKeyWord()));
        }

        if (dto.getUnitType() != null && !dto.getUnitType().isBlank()) {
            spec = spec.and(ComedianSpecification.unitType(dto.getUnitType()));
        }

        if (dto.getAgency() != null && !dto.getAgency().isBlank()) {
            spec = spec.and(ComedianSpecification.agency(dto.getAgency()));
        }

        return comedianProfileRepository.findAll(spec,pageable)
                .map(cp -> new ComedianListItemDto(
                        cp.getAccountId(),
                        cp.getAccount().getDisplayName(),
                        cp.getUnitType().getLabel(),
                        cp.getUnitType().getBadgeClass(),
                        cp.getAgency()
                ));
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
                            lp.getLive().getLiveType().getLabel(),
                            lp.getLive().getLiveType().getBadgeClass(),
                            lp.getLive().getPriceAdvance(),
                            lp.getLive().getPriceDoor()
                    )).toList();


            // 作ったlivesを渡す
            return new ComedianDetailDto(
                    comedian.getAccount().getDisplayName(),
                    comedian.getUnitType().getLabel(),
                    comedian.getUnitType().getBadgeClass(),
                    comedian.getAgency(),
                    comedian.getMemberNames(),
                    comedian.getAccount().getProfileImageUrl(),
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

    public List<ComedianSearchResultDto> searchComedians(String q) {
        return comedianProfileRepository.findByAccount_DisplayNameContainingIgnoreCase(q).stream()
                .map(cp -> new ComedianSearchResultDto(
                        cp.getAccountId(),
                        cp.getAccount().getDisplayName(),
                        cp.getUnitType().getLabel(),
                        cp.getAgency()
                )).toList();
    }
}
