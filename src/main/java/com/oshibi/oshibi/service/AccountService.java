package com.oshibi.oshibi.service;

import com.oshibi.oshibi.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Long getAccountIdByEmail(String email) {
        return accountRepository.findByUser_Email(email)
                .orElseThrow(() -> new IllegalStateException("アカウントが見つかりません: " + email))
                .getAccountId();
    }
}
