package com.oshibi.oshibi.repository;

import com.oshibi.oshibi.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUser_Email(String email);
}
