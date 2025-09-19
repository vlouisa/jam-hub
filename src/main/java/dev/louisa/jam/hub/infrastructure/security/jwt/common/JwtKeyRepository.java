package dev.louisa.jam.hub.infrastructure.security.jwt.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JwtKeyRepository extends JpaRepository<JwtKeyEntity, UUID> {}