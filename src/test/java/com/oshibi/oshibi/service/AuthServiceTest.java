package com.oshibi.oshibi.service;

import com.oshibi.oshibi.dto.RegisterDto;
import com.oshibi.oshibi.repository.AccountRepository;
import com.oshibi.oshibi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void メール重複の場合は例外が投げられる() {
        RegisterDto dto = new RegisterDto();
        dto.setEmail("test@test.com");
        dto.setPassword("password");
        dto.setPasswordConfirm("password");
        dto.setAccountType("FAN");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(new com.oshibi.oshibi.domain.entity.User()));

        assertThrows(IllegalArgumentException.class, () -> authService.register(dto));
    }

    @Test
    void パスワード不一致の場合は例外が投げられる() {
        RegisterDto dto = new RegisterDto();
        dto.setEmail("test@test.com");
        dto.setPassword("password");
        dto.setPasswordConfirm("different");
        dto.setAccountType("FAN");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> authService.register(dto));
    }

    @Test
    void accountTypeが不正の場合は例外が投げられる() {
        RegisterDto dto = new RegisterDto();
        dto.setEmail("test@test.com");
        dto.setPassword("password");
        dto.setPasswordConfirm("password");
        dto.setAccountType("INVALID");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> authService.register(dto));
    }

    @Test
    void 正常登録でuserRepositoryのsaveが呼ばれる() {
        RegisterDto dto = new RegisterDto();
        dto.setEmail("test@test.com");
        dto.setPassword("password");
        dto.setPasswordConfirm("password");
        dto.setAccountType("FAN");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("password"))
                .thenReturn("hashedPassword");

        authService.register(dto);

        verify(userRepository, times(1)).save(any());
    }
}
