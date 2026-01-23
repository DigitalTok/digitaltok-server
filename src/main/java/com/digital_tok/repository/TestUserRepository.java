package com.digital_tok.repository;

import com.digital_tok.domain.TestUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestUserRepository extends JpaRepository<TestUser, Long> {
    // TestUser 엔티티에 대한 기본 CRUD 메서드 제공 (findById, save, delete 등)
}
