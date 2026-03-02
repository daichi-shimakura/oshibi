package com.oshibi.oshibi.repository;

import com.oshibi.oshibi.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
