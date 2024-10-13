package io.greenCode.users_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.greenCode.users_management.entity.OurUsers;

public interface UsersRepository extends JpaRepository {
    Optional<OurUsers> findByEmail(String email);
}
