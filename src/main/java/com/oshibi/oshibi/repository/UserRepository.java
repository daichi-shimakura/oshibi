package com.oshibi.oshibi.repository;

import com.oshibi.oshibi.domain.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"account"})
    Optional<User> findByEmail(String email);
}
