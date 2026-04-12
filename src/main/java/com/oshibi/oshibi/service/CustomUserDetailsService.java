package com.oshibi.oshibi.service;

import com.oshibi.oshibi.domain.entity.User;
import com.oshibi.oshibi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. emailでUserを検索
        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));
        // 3. 見つかればUserDetailsオブジェクトを返す
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .roles(user.getAccount().getAccountType().toString())
                .build();
    }
}
