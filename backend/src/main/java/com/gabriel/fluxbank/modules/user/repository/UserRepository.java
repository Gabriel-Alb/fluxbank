package com.gabriel.fluxbank.modules.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabriel.fluxbank.modules.user.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmailLookupHash(byte[] emailLookupHash);

    boolean existsByCpfLookupHash(byte[] cpfLookupHash);

    Optional<User> findByEmailLookupHash(byte[] emailLookupHash);
}