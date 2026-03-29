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
        Optional<User> userOpt = userRepository.findByEmail(username);
        // 2. 見つからなければUsernameNotFoundExceptionをthrow
        User user = userOpt.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        // 3. 見つかればUserDetailsオブジェクトを返す
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .roles(user.getAccount().getAccountType().toString())
                .build();
    }

}
