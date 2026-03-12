package com.oshibi.oshibi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/lives", "/lives/**", "/comedians/**", "/comedians/detail/**",
                                "/login", "/register")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/lives", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/lives")
                        .permitAll()
                );

        return http.build();
    }

    // パスワードエンコーダーのBean定義（パスワードのハッシュ化を自動で行う）
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
