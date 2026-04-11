package com.oshibi.oshibi.service;

import com.oshibi.oshibi.domain.entity.LivePerformer;
import com.oshibi.oshibi.dto.ComedianLiveRequestDto;
import com.oshibi.oshibi.repository.LivePerformerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LiveServiceTest {
    @Mock
    private LivePerformerRepository livePerformerRepository;

    @InjectMocks
    private LiveService liveService;

    @Test
    void LivePerformerテーブルが見つからなかった場合例外が投げられる() {
        long liveId = 1;
        long accountId = 2;
        var dto = new ComedianLiveRequestDto();

        when(livePerformerRepository.findByLive_LiveIdAndComedian_AccountId(liveId, accountId))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> liveService.saveComedianLive(liveId, accountId, dto));
    }

    @Test
    void 正常登録でlivePerformerRepositoryのsaveが呼ばれる() {
        long liveId = 1;
        long accountId = 2;
        var dto = new ComedianLiveRequestDto();
        dto.setStatus("TENTATIVE");

        when(livePerformerRepository.findByLive_LiveIdAndComedian_AccountId(liveId, accountId))
                .thenReturn(Optional.of(new LivePerformer()));

        liveService.saveComedianLive(liveId, accountId, dto);

        verify(livePerformerRepository, times(1)).save(any(LivePerformer.class));
    }
}
