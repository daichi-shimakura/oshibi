package com.oshibi.oshibi.service;

import com.oshibi.oshibi.domain.entity.Live;
import com.oshibi.oshibi.domain.entity.LivePerformer;
import com.oshibi.oshibi.domain.entity.Venue;
import com.oshibi.oshibi.dto.*;
import com.oshibi.oshibi.repository.*;
import lombok.RequiredArgsConstructor;
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
                live.getLiveType(),
                live.getPriceAdvance(),
                live.getPriceDoor(),
                live.getLivePerformers().stream().filter(lp -> !lp.getComedian().getAccountId().equals(accountId)).map(lp -> new PerformerDto(
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
                live.getVenue().getGoogleMapsUrl(),
                comedianLiveOpt.getComedian().getAccountId(),
                comedianLiveOpt.getComedian().getAccount().getDisplayName(),
                comedianLiveOpt.getPreComment(),
                comedianLiveOpt.getNetaCount(),
                comedianLiveOpt.getNetaType(),
                comedianLiveOpt.getStatus()
        ));
    }

    public Long save(LiveRequestDto liveRequestDto,String email){
        //ライブパフォーマーがライブIdをFKにしているためライブから登録
        //liveRepositoryで使えるようにDtoをエンティティ型に変換する
        var live =  new Live();
        live.setLiveId(liveRequestDto.getLiveId());
        live.setCreatedBy(accountRepository.findByUser_Email(email).orElseThrow());
        live.setVenue(venueRepository.findById(liveRequestDto.getVenueId()).orElseThrow());
        live.setTitle(liveRequestDto.getTitle());
        live.setLiveType(liveRequestDto.getLiveType());
        live.setDescription(liveRequestDto.getDescription());
        live.setDate(liveRequestDto.getDate());
        live.setOpenTime(liveRequestDto.getOpenTime());
        live.setStartTime(liveRequestDto.getStartTime());
        live.setPriceAdvance(liveRequestDto.getPriceAdvance());
        live.setPriceDoor(liveRequestDto.getPriceDoor());
        live.setTicketMethod(liveRequestDto.getTicketMethod());
        live.setHasStreaming(liveRequestDto.getHasStreaming());
        live.setStreamingPrice(liveRequestDto.getStreamingPrice());
        live.setStreamingStartDate(liveRequestDto.getStreamingStartDate());
        live.setStreamingEndDate(liveRequestDto.getStreamingEndDate());
        live.setFlyerUrl(liveRequestDto.getFlyerUrl());
        liveRepository.save(live);

        if (liveRequestDto.getPerformerAccountIds() != null && !liveRequestDto.getPerformerAccountIds().isEmpty()) {
        //livePerformerを作成してlivePerformerに登録
        var livePerformers = liveRequestDto.getPerformerAccountIds().stream().map(id -> {
                    var performer = new LivePerformer();
                    performer.setComedian(comedianProfileRepository.findById(id).orElseThrow());
                    performer.setLive(live);
                    performer.setDisplayOrder(liveRequestDto.getPerformerAccountIds().indexOf(id));
                    performer.setStatus("TENTATIVE");
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
        comedianLiveOpt.setStatus(dto.getStatus());
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
                live.getLiveType(),
                live.getDescription(),
                live.getDate(),
                live.getOpenTime(),
                live.getStartTime(),
                live.getPriceAdvance(),
                live.getPriceDoor(),
                live.getTicketMethod(),
                live.getHasStreaming(),
                live.getStreamingPrice(),
                live.getStreamingStartDate(),
                live.getStreamingEndDate(),
                live.getFlyerUrl(),
                live.getLivePerformers().stream().map(lp -> new PerformerDto(
                        lp.getComedian().getAccountId(),
                        lp.getComedian().getAccount().getDisplayName(),
                        lp.getDisplayOrder()
                )).toList()
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
                        live.getLiveType(),
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
                            live.getLiveType(),
                            live.getPriceAdvance(),
                            live.getPriceDoor(),
                            List.of(),  // comedianNames（仮置き）
                            0           // otherPerformerCount（仮置き）
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
