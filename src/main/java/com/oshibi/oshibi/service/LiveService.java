package com.oshibi.oshibi.service;

import com.oshibi.oshibi.domain.entity.Live;
import com.oshibi.oshibi.domain.entity.LivePerformer;
import com.oshibi.oshibi.domain.entity.LiveType;
import com.oshibi.oshibi.domain.entity.PerformerStatus;
import com.oshibi.oshibi.domain.entity.TicketMethod;
import com.oshibi.oshibi.domain.entity.Venue;
import com.oshibi.oshibi.dto.ComedianLiveDetailDto;
import com.oshibi.oshibi.dto.ComedianLiveRequestDto;
import com.oshibi.oshibi.dto.LiveDetailDto;
import com.oshibi.oshibi.dto.LiveFormDto;
import com.oshibi.oshibi.dto.LiveListItemDto;
import com.oshibi.oshibi.dto.LiveSearchDto;
import com.oshibi.oshibi.dto.PerformerDto;
import com.oshibi.oshibi.repository.AccountRepository;
import com.oshibi.oshibi.repository.ComedianProfileRepository;
import com.oshibi.oshibi.repository.LivePerformerRepository;
import com.oshibi.oshibi.repository.LiveRepository;
import com.oshibi.oshibi.repository.LiveSpecification;
import com.oshibi.oshibi.repository.VenueRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LiveService {
    private final LiveRepository liveRepository;
    private final LivePerformerRepository livePerformerRepository;
    private final VenueRepository venueRepository;
    private final AccountRepository accountRepository;
    private final ComedianProfileRepository comedianProfileRepository;

    public Page<LiveListItemDto> search(LiveSearchDto dto, Pageable pageable) {
        Specification<Live> spec = (root, query, cb) -> cb.conjunction();

        if (dto.getKeyword() != null && !dto.getKeyword().isBlank()) {
            spec = spec.and(LiveSpecification.keywordSearch(dto.getKeyword()));
        }
        if (dto.getDateFrom() != null) {
            spec = spec.and(LiveSpecification.dateAfter(dto.getDateFrom()));
        }
        if (dto.getDateTo() != null) {
            spec = spec.and(LiveSpecification.dateBefore(dto.getDateTo()));
        }
        if (!dto.isHasPast()) {
            spec = spec.and(LiveSpecification.futureOnly());
        }
        if (dto.getPrefecture() != null && !dto.getPrefecture().isBlank()) {
            spec = spec.and(LiveSpecification.prefecture(dto.getPrefecture()));
        }
        if (dto.getLiveType() != null && !dto.getLiveType().isBlank()) {
            spec = spec.and(LiveSpecification.liveType(dto.getLiveType()));
        }
        if (dto.getMaxPrice() != null) {
            spec = spec.and(LiveSpecification.maxPrice(dto.getMaxPrice()));
        }
        if (dto.getStartTimeSlot() != null) {
            spec = spec.and(LiveSpecification.startTimeSlot(dto.getStartTimeSlot()));
        }

        return liveRepository.findAll(spec, pageable)
                .map(live -> {
                    Venue venue = live.getVenue();
                    return new LiveListItemDto(
                            live.getLiveId(),
                            live.getTitle(),
                            live.getDate(),
                            live.getOpenTime(),
                            live.getStartTime(),
                            venue != null ? venue.getName() : null,
                            venue != null ? venue.getPrefecture() : null,
                            live.getLiveType().getLabel(),
                            live.getLiveType().getBadgeClass(),
                            live.getPriceAdvance(),
                            live.getPriceDoor(),
                            live.getLivePerformers().stream().map(lp ->
                                    lp.getComedian() != null
                                            ? lp.getComedian().getAccount().getDisplayName()
                                            : lp.getGuestName()
                            ).toList(),
                            Math.max(0, live.getLivePerformers().size() - 20)
                    );
                });
    }

    public Optional<LiveDetailDto> findById(Long liveId) {
        return liveRepository.findById(liveId).map(live -> {
            Venue venue = live.getVenue();
            return new LiveDetailDto(
                    live.getLiveId(),
                    live.getTitle(),
                    live.getDate(),
                    live.getOpenTime(),
                    live.getStartTime(),
                    venue != null ? venue.getName() : null,
                    venue != null ? venue.getPrefecture() : null,
                    live.getLiveType().getLabel(),
                    live.getLiveType().getBadgeClass(),
                    live.getPriceAdvance(),
                    live.getPriceDoor(),
                    live.getLivePerformers().stream().map(lp -> new PerformerDto(
                            lp.getComedian() != null ? lp.getComedian().getAccountId() : null,
                            lp.getComedian() != null ? lp.getComedian().getAccount().getDisplayName() : lp.getGuestName(),
                            lp.getDisplayOrder(),
                            lp.getStatus().name())).toList(),
                    live.getTicketMethod() != null ? live.getTicketMethod().getLabel() : null,
                    live.getHasStreaming(),
                    live.getStreamingPrice(),
                    live.getStreamingStartDate(),
                    live.getStreamingEndDate(),
                    live.getFlyerUrl(),
                    live.getCreatedBy().getAccountId(),
                    live.getCreatedBy().getDisplayName(),
                    live.getDescription(),
                    venue != null ? venue.getAddress() : null,
                    venue != null ? venue.getNearestStation() : null,
                    venue != null ? venue.getGoogleMapsUrl() : null,
                    live.getTicketUrl()
            );
        });
    }

    public Optional<ComedianLiveDetailDto> findByLiveIdAndAccountId(Long liveId, Long accountId) {
        var livePerformer = livePerformerRepository.findByLive_LiveIdAndComedian_AccountId(liveId, accountId).orElseThrow();

        return liveRepository.findById(liveId).map(live -> {
            Venue venue = live.getVenue();
            return new ComedianLiveDetailDto(
                    live.getLiveId(),
                    live.getTitle(),
                    live.getDate(),
                    live.getOpenTime(),
                    live.getStartTime(),
                    venue != null ? venue.getName() : null,
                    venue != null ? venue.getPrefecture() : null,
                    live.getLiveType().getLabel(),
                    live.getLiveType().getBadgeClass(),
                    live.getPriceAdvance(),
                    live.getPriceDoor(),
                    live.getLivePerformers().stream()
                            .filter(lp -> lp.getComedian() == null || !lp.getComedian().getAccountId().equals(accountId))
                            .map(lp -> new PerformerDto(
                                    lp.getComedian() != null ? lp.getComedian().getAccountId() : null,
                                    lp.getComedian() != null ? lp.getComedian().getAccount().getDisplayName() : lp.getGuestName(),
                                    lp.getDisplayOrder(),
                                    lp.getStatus().name())).toList(),
                    live.getTicketMethod() != null ? live.getTicketMethod().getLabel() : null,
                    live.getTicketUrl(),
                    live.getHasStreaming(),
                    live.getStreamingPrice(),
                    live.getStreamingStartDate(),
                    live.getStreamingEndDate(),
                    live.getFlyerUrl(),
                    live.getCreatedBy().getAccountId(),
                    live.getCreatedBy().getDisplayName(),
                    live.getDescription(),
                    venue != null ? venue.getAddress() : null,
                    venue != null ? venue.getNearestStation() : null,
                    venue != null ? venue.getGoogleMapsUrl() : null,
                    livePerformer.getComedian().getAccountId(),
                    livePerformer.getComedian().getAccount().getDisplayName(),
                    livePerformer.getPreComment(),
                    livePerformer.getNetaCount(),
                    livePerformer.getNetaType(),
                    livePerformer.getStatus().getLabel()
            );
        });
    }

    @Transactional
    public Long save(LiveFormDto liveFormDto, String email) {
        var live = new Live();
        live.setLiveId(liveFormDto.getLiveId());
        live.setCreatedBy(accountRepository.findByUser_Email(email).orElseThrow());
        live.setVenue(
                liveFormDto.getVenueId() != null
                        ? venueRepository.findById(liveFormDto.getVenueId()).orElseThrow()
                        : null
        );
        live.setTitle(liveFormDto.getTitle());
        live.setLiveType(LiveType.valueOf(liveFormDto.getLiveType()));
        live.setDescription(liveFormDto.getDescription());
        live.setDate(liveFormDto.getDate());
        live.setOpenTime(liveFormDto.getOpenTime());
        live.setStartTime(liveFormDto.getStartTime());
        live.setPriceAdvance(liveFormDto.getPriceAdvance());
        live.setPriceDoor(liveFormDto.getPriceDoor());
        live.setTicketMethod(
                liveFormDto.getTicketMethod() != null && !liveFormDto.getTicketMethod().isBlank()
                        ? TicketMethod.valueOf(liveFormDto.getTicketMethod())
                        : null
        );
        live.setHasStreaming(liveFormDto.getHasStreaming());
        live.setStreamingPrice(liveFormDto.getStreamingPrice());
        live.setStreamingStartDate(liveFormDto.getStreamingStartDate());
        live.setStreamingEndDate(liveFormDto.getStreamingEndDate());
        live.setFlyerUrl(liveFormDto.getFlyerUrl());
        live.setTicketUrl(liveFormDto.getTicketUrl());
        if (liveFormDto.getLiveId() != null) {
            livePerformerRepository.deleteByLive_LiveId(liveFormDto.getLiveId());
            livePerformerRepository.flush();
        }
        liveRepository.save(live);

        if (liveFormDto.getPerformerAdds() != null && !liveFormDto.getPerformerAdds().isEmpty()) {
            var livePerformers = liveFormDto.getPerformerAdds().stream().map(id -> {
                var performer = new LivePerformer();
                if (id.getAccountId() != null) {
                    performer.setComedian(comedianProfileRepository.findById(id.getAccountId()).orElseThrow());
                } else {
                    performer.setGuestName(id.getGuestName());
                }
                performer.setLive(live);
                performer.setDisplayOrder(liveFormDto.getPerformerAdds().indexOf(id));
                performer.setStatus(PerformerStatus.TENTATIVE);
                return performer;
            }).toList();
            livePerformerRepository.saveAll(livePerformers);
        }
        return live.getLiveId();
    }

    public void saveComedianLive(Long liveId, Long accountId, ComedianLiveRequestDto dto) {
        var livePerformer = livePerformerRepository.findByLive_LiveIdAndComedian_AccountId(liveId, accountId).orElseThrow();
        livePerformer.setStatus(PerformerStatus.valueOf(dto.getStatus()));
        livePerformer.setNetaCount(dto.getNetaCount());
        livePerformer.setNetaType(dto.getNetaType());
        livePerformer.setPreComment(dto.getPreComment());
        livePerformerRepository.save(livePerformer);
    }

    public Optional<LiveFormDto> findLiveForEdit(Long liveId) {
        return liveRepository.findById(liveId).map(live -> {
            Venue venue = live.getVenue();
            return new LiveFormDto(
                    live.getCreatedBy().getAccountId(),
                    venue != null ? venue.getVenueId() : null,
                    venue != null ? venue.getName() : null,
                    venue != null ? venue.getPrefecture() : null,
                    venue != null ? venue.getAddress() : null,
                    venue != null ? venue.getNearestStation() : null,
                    venue != null ? venue.getGoogleMapsUrl() : null,
                    live.getLiveId(),
                    live.getTitle(),
                    live.getLiveType().name(),
                    live.getDescription(),
                    live.getDate(),
                    live.getOpenTime(),
                    live.getStartTime(),
                    live.getPriceAdvance(),
                    live.getPriceDoor(),
                    live.getTicketMethod() != null ? live.getTicketMethod().name() : null,
                    live.getHasStreaming(),
                    live.getStreamingPrice(),
                    live.getStreamingStartDate(),
                    live.getStreamingEndDate(),
                    live.getFlyerUrl(),
                    live.getTicketUrl(),
                    live.getLivePerformers().stream().map(lp -> new PerformerDto(
                            lp.getComedian() != null ? lp.getComedian().getAccountId() : null,
                            lp.getComedian() != null ? lp.getComedian().getAccount().getDisplayName() : lp.getGuestName(),
                            lp.getDisplayOrder(),
                            null
                    )).toList(),
                    null
            );
        });
    }

    public List<LiveListItemDto> getOwnedLives(Long accountId) {
        return liveRepository.findByCreatedBy_AccountIdAndDateGreaterThanEqualOrderByDateAsc(accountId, LocalDate.now()).stream()
                .map(live -> {
                    Venue venue = live.getVenue();
                    return new LiveListItemDto(
                            live.getLiveId(),
                            live.getTitle(),
                            live.getDate(),
                            live.getOpenTime(),
                            live.getStartTime(),
                            venue != null ? venue.getName() : null,
                            venue != null ? venue.getPrefecture() : null,
                            live.getLiveType().getLabel(),
                            live.getLiveType().getBadgeClass(),
                            live.getPriceAdvance(),
                            live.getPriceDoor(),
                            List.of(),
                            0
                    );
                }).toList();
    }

    public List<LiveListItemDto> getPerformingLives(Long accountId) {
        return livePerformerRepository.findByComedian_AccountIdAndLive_DateGreaterThanEqual(accountId, LocalDate.now()).stream()
                .map(lp -> {
                    var live = lp.getLive();
                    Venue venue = live.getVenue();
                    return new LiveListItemDto(
                            live.getLiveId(),
                            live.getTitle(),
                            live.getDate(),
                            live.getOpenTime(),
                            live.getStartTime(),
                            venue != null ? venue.getName() : null,
                            venue != null ? venue.getPrefecture() : null,
                            live.getLiveType().getLabel(),
                            live.getLiveType().getBadgeClass(),
                            live.getPriceAdvance(),
                            live.getPriceDoor(),
                            live.getLivePerformers().stream().map(livePerformer ->
                                    livePerformer.getComedian() != null
                                            ? livePerformer.getComedian().getAccount().getDisplayName()
                                            : livePerformer.getGuestName()).toList(),
                            Math.max(0, live.getLivePerformers().size() - 20)
                    );
                }).toList();
    }

    @Transactional
    public void delete(Long liveId, String email) {
        checkAuth(email, liveId);
        livePerformerRepository.deleteByLive_LiveId(liveId);
        liveRepository.deleteById(liveId);
    }

    public List<Venue> findAllVenues() {
        return venueRepository.findAll();
    }

    public void checkAuth(String email, Long liveId) {
        var createdById = liveRepository.findById(liveId).orElseThrow().getCreatedBy().getAccountId();
        var loginAccountId = accountRepository.findByUser_Email(email).orElseThrow().getAccountId();
        if (!createdById.equals(loginAccountId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized");
        }
    }

    public void checkAuthComedianLiveAccount(String email, Long accountId) {
        var loginAccountId = accountRepository.findByUser_Email(email).orElseThrow().getAccountId();
        if (!accountId.equals(loginAccountId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized");
        }
    }

    public void restorePerformers(LiveFormDto dto) {
        if (dto.getPerformerAdds() == null) return;
        dto.setPerformers(
                dto.getPerformerAdds().stream()
                        .map(add -> new PerformerDto(
                                add.getAccountId(),
                                add.getAccountId() != null ? add.getDisplayName() : add.getGuestName(),
                                null,
                                null))
                        .toList()
        );
    }
}