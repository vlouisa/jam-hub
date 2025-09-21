package dev.louisa.jam.hub.infrastructure.security.auth;

import dev.louisa.jam.hub.testsupport.base.BaseInfraStructureTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BCryptPasswordHasherTest extends BaseInfraStructureTest {
    @Test
    void shouldHashPasswordBCryptStyle(){
        var passwordHasher = new BCryptPasswordHasher();
        var hashedPassword = passwordHasher.hash("mysecretpassword");
        
        assertThat(hashedPassword).startsWith("$2a$");
        assertThat(passwordHasher.matches("mysecretpassword", hashedPassword)).isTrue();
        assertThat(passwordHasher.matches("wrongpassword", hashedPassword)).isFalse();
    }
}