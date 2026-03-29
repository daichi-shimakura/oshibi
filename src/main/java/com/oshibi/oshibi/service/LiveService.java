package com.oshibi.oshibi.service;

import com.oshibi.oshibi.domain.entity.*;
import com.oshibi.oshibi.dto.*;
import com.oshibi.oshibi.repository.*;
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

        return liveRepository.findAll(spec,pageable)
                .map(live -> new LiveListItemDto(
                            live.getLiveId(),
                            live.getTitle(),
                            live.getDate(),
                            live.getOpenTime(),
                            live.getStartTime(),
                            live.getVenue().getName(),
                            live.getVenue().getPrefecture(),
                            live.getLiveType().getLabel(),
                            live.getPriceAdvance(),
                            live.getPriceDoor(),
                            live.getLivePerformers().stream().map(lp ->
                                    lp.getComedian() != null
                                            ? lp.getComedian().getAccount().getDisplayName()
                                            : lp.getGuestName()
                            ).toList(),
                            Math.max(0, live.getLivePerformers().size() - 20)
                    ));
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
                live.getLiveType().getLabel(),
                live.getPriceAdvance(),
                live.getPriceDoor(),
                live.getLivePerformers().stream().map(lp -> new PerformerDto(
                        lp.getComedian() != null
                                ? lp.getComedian().getAccountId()
                                : null,
                        lp.getComedian() != null
                                ? lp.getComedian().getAccount().getDisplayName()
                                : lp.getGuestName(),
                        lp.getDisplayOrder())).toList(),
                live.getTicketMethod() != null ? live.getTicketMethod().getLabel() : null,
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

    public Optional<ComedianLiveDetailDto> findByLiveIdAndAccountId(Long liveId,Long accountId) {

        var comedianLiveOpt = livePerformerRepository.findByLive_LiveIdAndComedian_AccountId(liveId, accountId).orElseThrow();

        return liveRepository.findById(liveId).map(live -> new ComedianLiveDetailDto(
                live.getLiveId(),
                live.getTitle(),
                live.getDate(),
                live.getOpenTime(),
                live.getStartTime(),
                live.getVenue().getName(),
                live.getVenue().getPrefecture(),
                live.getLiveType().getLabel(),
                live.getPriceAdvance(),
                live.getPriceDoor(),
                live.getLivePerformers().stream().filter(lp -> (lp.getComedian() == null ||
                        (!lp.getComedian().getAccountId().equals(accountId)))
                ).map(lp -> new PerformerDto(
                        lp.getComedian() != null
                                ? lp.getComedian().getAccountId()
                                : null,
                        lp.getComedian() != null
                                ? lp.getComedian().getAccount().getDisplayName()
                                : lp.getGuestName(),
                        lp.getDisplayOrder())).toList(),
                live.getTicketMethod() != null ? live.getTicketMethod().getLabel() : null,
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
                live.getVenue().getGoogleMapsUrl(),
                comedianLiveOpt.getComedian().getAccountId(),
                comedianLiveOpt.getComedian().getAccount().getDisplayName(),
                comedianLiveOpt.getPreComment(),
                comedianLiveOpt.getNetaCount(),
                comedianLiveOpt.getNetaType(),
                comedianLiveOpt.getStatus().getLabel()
        ));
    }

    public Long save(LiveFormDto liveFormDto,String email){
        //ライブパフォーマーがライブIdをFKにしているためライブから登録
        //liveRepositoryで使えるようにDtoをエンティティ型に変換する
        var live =  new Live();
        live.setLiveId(liveFormDto.getLiveId());
        live.setCreatedBy(accountRepository.findByUser_Email(email).orElseThrow());
        live.setVenue(venueRepository.findById(liveFormDto.getVenueId()).orElseThrow());
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
        liveRepository.save(live);

        if (liveFormDto.getPerformerAdds() != null && !liveFormDto.getPerformerAdds().isEmpty()) {
        //livePerformerを作成してlivePerformerに登録
        var livePerformers = liveFormDto.getPerformerAdds().stream().map(id -> {
                    var performer = new LivePerformer();
                    if (id.getAccountId() != null) {
                        performer.setComedian(comedianProfileRepository.findById(id.getAccountId()).orElseThrow());
                        performer.setLive(live);
                        performer.setDisplayOrder(liveFormDto.getPerformerAdds().indexOf(id));
                        performer.setStatus(PerformerStatus.TENTATIVE);
                    } else {
                        performer.setGuestName(id.getGuestName());
                        performer.setLive(live);
                        performer.setDisplayOrder(liveFormDto.getPerformerAdds().indexOf(id));
                        performer.setStatus(PerformerStatus.TENTATIVE);
                    }
                    return performer;
                }
                ).toList();
        livePerformerRepository.saveAll(livePerformers);
        }
        return live.getLiveId();
    }

    public void saveComedianLive(Long liveId, Long accountId,ComedianLiveRequestDto dto){
        //livePerformerエンティティを作成
        //エンティティにDtoの値をセットして保存
        //liveIdとaccountIdでLivePerformerテーブルを検索→あったらそこにComedianLiveRequestDtoの値を入れる
        var comedianLiveOpt = livePerformerRepository.findByLive_LiveIdAndComedian_AccountId(liveId, accountId).orElseThrow();
        comedianLiveOpt.setStatus(PerformerStatus.valueOf(dto.getStatus()));
        comedianLiveOpt.setNetaCount(dto.getNetaCount());
        comedianLiveOpt.setNetaType(dto.getNetaType());
        comedianLiveOpt.setPreComment(dto.getPreComment());

        livePerformerRepository.save(comedianLiveOpt);
    }

    public Optional<LiveFormDto> findLiveForEdit(Long liveId){
        return liveRepository.findById(liveId).map(live -> new LiveFormDto(
                live.getCreatedBy().getAccountId(),
                live.getVenue().getVenueId(),
                live.getVenue().getName(),
                live.getVenue().getPrefecture(),
                live.getVenue().getAddress(),
                live.getVenue().getNearestStation(),
                live.getVenue().getGoogleMapsUrl(),
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
                live.getLivePerformers().stream().map(lp -> new PerformerDto(
                        lp.getComedian() != null
                                ? lp.getComedian().getAccountId()
                                : null,
                        lp.getComedian() != null
                                ? lp.getComedian().getAccount().getDisplayName()
                                : lp.getGuestName(),
                        lp.getDisplayOrder()
                )).toList(),
                null
        ));
    }

    public List<LiveListItemDto> getOwnedLives(Long accountId){
        return liveRepository.findByCreatedBy_AccountIdAndDateGreaterThanEqual(accountId,LocalDate.now()).stream()
                .map(live -> new LiveListItemDto(
                        live.getLiveId(),
                        live.getTitle(),
                        live.getDate(),
                        live.getOpenTime(),
                        live.getStartTime(),
                        live.getVenue().getName(),
                        live.getVenue().getPrefecture(),
                        live.getLiveType().getLabel(),
                        live.getPriceAdvance(),
                        live.getPriceDoor(),
                        List.of(),  // comedianNames（仮置き）
                        0           // otherPerformerCount（仮置き）
                )).toList();
    }

    public List<LiveListItemDto> getPerformingLives(Long accountId){
        return livePerformerRepository.findByComedian_AccountIdAndLive_DateGreaterThanEqual(accountId,LocalDate.now()).stream()
                .map(lp -> {
                    var live = lp.getLive();
                    return new LiveListItemDto(
                            live.getLiveId(),
                            live.getTitle(),
                            live.getDate(),
                            live.getOpenTime(),
                            live.getStartTime(),
                            live.getVenue().getName(),
                            live.getVenue().getPrefecture(),
                            live.getLiveType().getLabel(),
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

    public List<Venue> findAllVenues() {
        return venueRepository.findAll();
    }

    public void checkAuth(String email, Long liveId){

        var createdById = liveRepository.findById(liveId).orElseThrow().getCreatedBy().getAccountId();
        var loginAccountId = accountRepository.findByUser_Email(email).orElseThrow().getAccountId();

        if (!createdById.equals(loginAccountId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized");
        }
    }

    public void checkAuthComedianLiveAccount(String email, Long accountId){

        var loginAccountId = accountRepository.findByUser_Email(email).orElseThrow().getAccountId();

        if (!accountId.equals(loginAccountId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized");
        }
    }



}
