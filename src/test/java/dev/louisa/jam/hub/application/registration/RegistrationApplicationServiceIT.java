package dev.louisa.jam.hub.application.registration;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.louisa.jam.hub.application.exceptions.ApplicationException;
import dev.louisa.jam.hub.application.user.PasswordFactory;
import dev.louisa.jam.hub.domain.common.EmailAddress;
import dev.louisa.jam.hub.domain.registration.UserRegistration;
import dev.louisa.jam.hub.domain.registration.UserRegistrationId;
import dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainException;
import dev.louisa.jam.hub.domain.user.User;
import dev.louisa.jam.hub.domain.user.persistence.UserRepository;
import dev.louisa.jam.hub.testsupport.base.BaseApplicationIT;
import dev.louisa.jam.hub.domain.registration.persistence.UserRegistrationRepository;
import dev.louisa.victor.mail.pit.asserter.MailPitResponseAssert;
import dev.louisa.victor.mail.pit.docker.MailPitContainer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static dev.louisa.jam.hub.application.exceptions.ApplicationError.ENTITY_NOT_FOUND;
import static dev.louisa.jam.hub.application.exceptions.ApplicationError.USER_ALREADY_EXIST;
import static dev.louisa.jam.hub.domain.registration.exceptions.UserRegistrationDomainError.OTP_CODE_ALREADY_VERIFIED;
import static dev.louisa.jam.hub.testsupport.Factory.domain.aUser;
import static dev.louisa.jam.hub.testsupport.Factory.domain.aUserRegistration;
import static dev.louisa.jam.hub.testsupport.asserts.RegistrationAssert.assertThatRegistration;
import static dev.louisa.jam.hub.testsupport.asserts.UserAssert.assertThatUser;
import static dev.louisa.victor.mail.pit.api.MailPitApi.*;
import static java.time.Instant.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RegistrationApplicationServiceIT extends BaseApplicationIT {
    @Autowired
    private MailPitContainer mailPitContainer;
    
    @Autowired
    private UserRegistrationRepository userRegistrationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordFactory passwordFactory;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RegistrationApplicationService registrationApplicationService;

    @BeforeEach
    void setUp() {
        mailPitContainer.start();
    }
    
    @AfterEach
    void tearDown() {
        mailPitContainer.stop();
    }

    @Test
    void shouldCreateNewRegistration() throws JsonProcessingException {
        EmailAddress emailAddress = EmailAddress.builder().email("kate.capsize@mi-2.nl").build();

        var userRegistrationId = registrationApplicationService.register(emailAddress);

        var registration = userRegistrationRepository.findById(userRegistrationId).orElseThrow();
        assertThatRegistration(registration)
                .hasId(userRegistrationId)
                .hasEmail(emailAddress)
                .hasOtp(registration.getOtp())
                .hasExpiryAfter(now());


        awaitMessages(1);

        var response = fetchMessages(mailPitContainer.baseUri());
        MailPitResponseAssert.assertThatMailPitResponse(response)        
                .message(1)
                .hasRecipient("kate.capsize@mi-2.nl")
                .hasSubject("Welcome to JAM Hub! Please verify your email address");
    }


    @Test
    void shouldThrowWhenUserAlreadyExistsWithGivenEmailAddress() {
        var user = aUser
                .usingPasswordFactory(passwordFactory)
                .usingRepository(userRepository)
                .create();

        assertThatCode(() -> registrationApplicationService.register(user.getEmail()))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining(USER_ALREADY_EXIST.getMessage());
    }

    @Test
    void shouldVerifyRegistration() throws JsonProcessingException {
        var registration = aUserRegistration
                .usingRepository(userRegistrationRepository)
                .create();

        registrationApplicationService.verifyOtp(registration.getId(), registration.getOtp(), "MyP@ssW0rd!");

        final UserRegistration retrievedRegistration = userRegistrationRepository.findById(registration.getId()).orElseThrow();
        assertThatRegistration(retrievedRegistration)
                .hasId(registration.getId())
                .hasEmail(registration.getEmail())
                .hasOtp(registration.getOtp())
                .isVerified();

        final User retrievedUser = userRepository.findByEmail(registration.getEmail()).orElseThrow();
        assertThatUser(retrievedUser)
                .usingPasswordEncoder(passwordEncoder)
                .hasDisplayNameDerivedFromEmail()
                .hasPassword("MyP@ssW0rd!")
                .hasEmail(registration.getEmail());


        awaitMessages(1);
        var response = fetchMessages(mailPitContainer.baseUri());
        MailPitResponseAssert.assertThatMailPitResponse(response)
                .message(1)
                .hasRecipient(registration.getEmail().email())
                .hasSubject("Welcome to JAM Hub! Your account has been created");

    }

    @Test
    void shouldNotVerifyRegistrationWhenAlreadyVerified() {
        var registration = aUserRegistration
                .usingRepository(userRegistrationRepository)
                .createVerified();

        assertThatCode(() -> registrationApplicationService.verifyOtp(registration.getId(), registration.getOtp(), "MyP@ssW0rd!"))
                .isInstanceOf(UserRegistrationDomainException.class)
                .hasMessageContaining(OTP_CODE_ALREADY_VERIFIED.getMessage());

        final UserRegistration retrievedRegistration = userRegistrationRepository.findById(registration.getId()).orElseThrow();
        assertThatRegistration(retrievedRegistration)
                .hasId(registration.getId())
                .hasEmail(registration.getEmail())
                .hasOtp(registration.getOtp())
                .isVerifiedAt(registration.getVerifiedAt());


    }

    @Test
    void shouldThrowWhenRegistrationDoesNotExist() {
        assertThatCode(() -> registrationApplicationService.verifyOtp(UserRegistrationId.generate(), UUID.randomUUID(), "MyP@ssW0rd!"))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining(ENTITY_NOT_FOUND.getMessage());
    }
}
