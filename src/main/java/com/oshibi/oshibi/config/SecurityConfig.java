package com.oshibi.oshibi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/lives/new", "/lives/*/edit", "/lives/*/comedians/*/edit", "/my/lives").hasRole("LIVE_STAFF")
                        .requestMatchers("/lives", "/lives/**", "/comedians/**", "/comedians/detail/**", "/login", "/register", "/", "/403","/api/lives/extract-from-image")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, e) ->
                                response.sendRedirect("/403"))
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

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // パスワードエンコーダーのBean定義（パスワードのハッシュ化を自動で行う）
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
