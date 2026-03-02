package com.oshibi.oshibi.repository;

import com.oshibi.oshibi.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
