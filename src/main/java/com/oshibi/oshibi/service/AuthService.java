package com.oshibi.oshibi.service;

import com.oshibi.oshibi.domain.entity.Account;
import com.oshibi.oshibi.domain.entity.User;
import com.oshibi.oshibi.dto.PasswordChangeDto;
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

    public void changeEmail(String currentEmail, String newEmail){
            // 1. 現在のメールアドレスでUserを検索
            var userOpt = userRepository.findByEmail(currentEmail);
            var user = userOpt.orElseThrow(() -> new IllegalArgumentException("User not found with email: " + currentEmail));
            // 2. 新しいメールアドレスの重複チェック
            if (userRepository.findByEmail(newEmail).isPresent()) {
                throw new IllegalArgumentException("Email is already in use");
            }
            // 3. Userのメールアドレスを更新して保存
            user.setEmail(newEmail);
            userRepository.save(user);
    }

    public void changePassword(String email, PasswordChangeDto dto) {
        // 1. メールアドレスでUserを検索
        var userOpt = userRepository.findByEmail(email);
        var user = userOpt.orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        // 2. 現在のパスワードが正しいかチェック
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        // 3. 新しいパスワードと確認用パスワードが一致するかチェック
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        // 4. 新しいパスワードをハッシュ化して保存
        String newPasswordHash = passwordEncoder.encode(dto.getNewPassword());
        user.setPasswordHash(newPasswordHash);
        userRepository.save(user);
    }
}
