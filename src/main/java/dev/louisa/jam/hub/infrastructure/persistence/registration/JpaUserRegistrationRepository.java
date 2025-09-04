package dev.louisa.jam.hub.infrastructure.persistence.registration;

import dev.louisa.jam.hub.domain.registration.UserRegistration;
import dev.louisa.jam.hub.domain.registration.UserRegistrationId;
import dev.louisa.jam.hub.domain.registration.persistence.UserRegistrationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRegistrationRepository extends JpaRepository<UserRegistration, UserRegistrationId>, UserRegistrationRepository {}