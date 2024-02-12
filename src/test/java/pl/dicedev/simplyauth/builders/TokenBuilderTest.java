package pl.dicedev.simplyauth.builders;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenBuilderTest {

    @Test
    void shouldReturnTokenWithSemicolonAsSeparator() {
        // given
        TokenBuilder tokenBuilder = new TokenBuilder(";");

        // when
        String result = tokenBuilder.build();

        // then
        assertThat(result).isEqualTo("userName=null;validTime=null;scope=null;id=null");

    }

    @Test
    void shouldReturnTokenWithDotAsSeparator() {
        // given
        TokenBuilder tokenBuilder = new TokenBuilder(".");

        // when
        String result = tokenBuilder.build();

        // then
        assertThat(result).isEqualTo("userName=null.validTime=null.scope=null.id=null");

    }

    @Test
    void shouldReturnTokenWithAllDateInIt() {
        // given
        TokenBuilder tokenBuilder = new TokenBuilder(";");
        tokenBuilder
                .withId("111")
                .withUserName("Ala")
                .withScope("scope1")
                .withValidTime(1L);
        String expectedToken = "userName=Ala;validTime=.*;scope=scope1;id=111";

        // when
        String result = tokenBuilder.build();

        // then
        assertTrue(
                result.matches(expectedToken),
                "Expected token:\n" + expectedToken + "\nresult:\n" + result
        );
    }
}