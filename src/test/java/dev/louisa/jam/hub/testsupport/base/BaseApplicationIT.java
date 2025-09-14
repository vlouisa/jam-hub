package dev.louisa.jam.hub.testsupport.base;

import dev.louisa.victor.mail.pit.docker.MailPitContainer;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static dev.louisa.victor.mail.pit.api.MailPitApi.fetchMessages;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Tag("integration-test")
@Tag("application-layer")
@Transactional
@SpringBootTest
@ActiveProfiles("application-it")
// Disable security -> security should be tested in web/interfaces layer
@AutoConfigureMockMvc(addFilters = false)
public class BaseApplicationIT {

    @Autowired
    private MailPitContainer mailPitContainer;
    
    protected void awaitMessages(int numberOfMessages) {
        await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
            assertThat(fetchMessages(mailPitContainer.baseUri()).messages().size()).isEqualTo(numberOfMessages);
        });
    }

}
