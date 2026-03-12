package com.oshibi.oshibi.service;

import com.oshibi.oshibi.domain.entity.Account;
import com.oshibi.oshibi.domain.entity.User;
import com.oshibi.oshibi.dto.RegisterDto;
import com.oshibi.oshibi.repository.AccountRepository;
import com.oshibi.oshibi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterDto dto) {
        // 1. メールアドレスの重複チェック
            if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email is already in use");
            }
        // 2. パスワードのハッシュ化
        String passwordHash = passwordEncoder.encode(dto.getPassword());
        // 3. Userエンティティ作成・保存
            var user = new User();
            user.setEmail(dto.getEmail());
            user.setPasswordHash(passwordHash);
            userRepository.save(user);
        // 4. Accountエンティティ作成・保存
            var account = new Account();
            account.setUser(user);
            account.setAccountType(dto.getAccountType());
            account.setDisplayName(dto.getEmail());
            accountRepository.save(account);
    }
}
