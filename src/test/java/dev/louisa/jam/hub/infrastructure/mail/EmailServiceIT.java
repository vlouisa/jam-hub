package dev.louisa.jam.hub.infrastructure.mail;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.louisa.jam.hub.infrastructure.mail.exception.MailError;
import dev.louisa.jam.hub.infrastructure.mail.exception.MailException;
import dev.louisa.jam.hub.testsupport.base.BaseInfraStructureIT;
import dev.louisa.victor.mail.pit.api.MailPitApi;
import dev.louisa.victor.mail.pit.api.MailPitChaosTrigger;
import dev.louisa.victor.mail.pit.docker.MailPitContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.louisa.victor.mail.pit.asserter.MailPitResponseAssert.messagesFrom;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class EmailServiceIT extends BaseInfraStructureIT {
    private static final Email EMAIL =
            Email.builder()
                    .from(EmailAddress.builder()
                            .address("elaine.marley@mêléeisland.com")
                            .personal("Elaine Marley")
                            .build())
                    .to(EmailAddress.builder()
                            .address("guybrush.threepwood@lechuck.com")
                            .personal("Guybrush Threepwood")
                            .build())
                    .subject("Monkey Island II, the best game ever")
                    .body("""
                            <p>Ahoy Guybrush,</p>
                            <p>It is me, Elaine Marley - yes, the one who actually remembered where I left my rum.</p>
                            <p>Stay swashbuckling,</p>
                            <p>Cheers, Elaine</p>
                            """)

                    .build();
    
    @Autowired
    private MailPitContainer mailPitContainer;

    @Autowired
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        mailPitContainer.start();
    }

    @AfterEach
    void tearDown() {
        mailPitContainer.stop();
    }

    @Test
    void shouldSendMail() throws JsonProcessingException {
        emailService.sendEmail(EMAIL);

        messagesFrom(mailPitContainer.baseUri())
                .awaitMessages(1)
                .assertThat()
                .message(1)
                .hasSender("elaine.marley@mêléeisland.com")
                .hasRecipient("guybrush.threepwood@lechuck.com")
                .hasSubject("Monkey Island II, the best game ever")
                .bodySnippetContains("Ahoy Guybrush")
                .bodySnippetContains("Stay swashbuckling,");
    }

    @ParameterizedTest
    @CsvSource({
            "AUTHENTICATION, SMTP_AUTHENTICATION_ERROR",
            "SENDER, SMTP_SENDER_ERROR",
            "RECIPIENT, SMTP_SENDER_ERROR",
    })
    void shouldReportAuthenticationError(MailPitChaosTrigger trigger, MailError mailError) {
        MailPitApi.forceSmtpError(mailPitContainer.baseUri(), trigger);

        assertThatCode(() -> emailService.sendEmail(EMAIL))
                .isInstanceOf(MailException.class)
                .hasMessageContaining(mailError.getMessage());
    }
}