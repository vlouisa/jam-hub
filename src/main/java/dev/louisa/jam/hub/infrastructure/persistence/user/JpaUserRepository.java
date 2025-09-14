package dev.louisa.jam.hub.infrastructure.persistence.user;

import dev.louisa.jam.hub.domain.user.User;
import dev.louisa.jam.hub.domain.user.UserId;
import dev.louisa.jam.hub.domain.user.persistence.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<User, UserId>, UserRepository {}