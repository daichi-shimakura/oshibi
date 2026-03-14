package com.oshibi.oshibi.service;

import com.oshibi.oshibi.domain.entity.Account;
import com.oshibi.oshibi.domain.entity.ComedianProfile;
import com.oshibi.oshibi.dto.ProfileDto;
import com.oshibi.oshibi.repository.AccountRepository;
import com.oshibi.oshibi.repository.ComedianProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProfileService {

    private final AccountRepository accountRepository;
    private final ComedianProfileRepository comedianProfileRepository;

    public void saveProfile(String email, ProfileDto dto) {
        // Accountを作成
        //ログイン中のアカウントのIdを取得
        Account account = accountRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        //アカウントエンティティを更新
        account.setDisplayName(dto.getDisplayName());
        account.setDescription(dto.getDescription());
        account.setProfileImageUrl(dto.getProfileImageUrl());
        account.setXUrl(dto.getXUrl());
        account.setInstagramUrl(dto.getInstagramUrl());
        account.setYoutubeUrl(dto.getYoutubeUrl());
        account.setTiktokUrl(dto.getTiktokUrl());
        account.setNoteUrl(dto.getNoteUrl());
        account.setPodcastUrl(dto.getPodcastUrl());
        accountRepository.save(account);

        if ("LIVE_STAFF".equals(account.getAccountType())) {
            // ComedianProfileを作成
            ComedianProfile comedianProfile = comedianProfileRepository
                    .findById(account.getAccountId())
                    .orElse(new ComedianProfile());
            comedianProfile.setUnitType(dto.getUnitType());
            comedianProfile.setAgency(dto.getAgency());
            comedianProfile.setMemberNames(dto.getMemberNames());
            comedianProfile.setAccount(account);
            comedianProfileRepository.save(comedianProfile);
        }
    }
}
