package com.oshibi.oshibi.service;

import com.oshibi.oshibi.domain.entity.Account;
import com.oshibi.oshibi.domain.entity.AccountType;
import com.oshibi.oshibi.domain.entity.User;
import com.oshibi.oshibi.dto.PasswordChangeDto;
import com.oshibi.oshibi.dto.RegisterDto;
import com.oshibi.oshibi.repository.AccountRepository;
import com.oshibi.oshibi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("このメールアドレスはすでに使用されています");
        }

        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new IllegalArgumentException("パスワードが一致しません");
        }

        if (!List.of("FAN", "LIVE_STAFF").contains(dto.getAccountType())) {
            throw new IllegalArgumentException("不正なアカウント種別です");
        }

        String passwordHash = passwordEncoder.encode(dto.getPassword());

        var user = new User();
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordHash);
        userRepository.save(user);

        var account = new Account();
        account.setUser(user);
        account.setAccountType(AccountType.valueOf(dto.getAccountType()));
        account.setDisplayName(dto.getEmail());
        accountRepository.save(account);

        user.setAccount(account);
    }

    public void changeEmail(String currentEmail, String newEmail) {
        var userOpt = userRepository.findByEmail(currentEmail);
        var user = userOpt.orElseThrow(() -> new IllegalArgumentException("メールアドレスが見つかりません: " + currentEmail));

        if (userRepository.findByEmail(newEmail).isPresent()) {
            throw new IllegalArgumentException("このメールアドレスはすでに使用されています");
        }

        user.setEmail(newEmail);
        userRepository.save(user);
    }

    public void changePassword(String email, PasswordChangeDto dto) {
        var userOpt = userRepository.findByEmail(email);
        var user = userOpt.orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません: " + email));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("現在のパスワードが正しくありません");
        }

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("パスワードが一致しません");
        }

        String newPasswordHash = passwordEncoder.encode(dto.getNewPassword());
        user.setPasswordHash(newPasswordHash);
        userRepository.save(user);
    }
}
