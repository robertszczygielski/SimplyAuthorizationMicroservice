package pl.dicedev.simplyauth.service;

import org.junit.jupiter.api.Test;
import pl.dicedev.simplyauth.dto.AuthDto;
import pl.dicedev.simplyauth.exception.ValidationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthenticationServiceTest {

    private final AuthenticationService authenticationService = new AuthenticationService();

    @Test
    void shouldThrowAnExceptionWhenCredentialsContainsOnlyUserNameOrPassword() {
        // given

        // when
        ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> authenticationService.getAuthentication("test")
        );

        // then
        assertThat(validationException.getMessage()).isEqualTo("User or password are incorrect");

    }

    @Test
    void shouldReturnNotBlankToken() {
        // given

        // when
        AuthDto authDto = authenticationService.getAuthentication("test:test");

        // then
        assertThat(authDto.getToken()).isNotBlank();

    }

    @Test
    void shouldThrowExceptionWhenUserNameIsIncorrect() {
        // given

        // when
        ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> authenticationService.getAuthentication("test1:test")
        );

        // then
        assertThat(validationException.getMessage()).isEqualTo("User/password incorrect");
    }

    @Test
    void shouldThrowAnExceptionWhenPasswordIsIncorrect() {
        // given

        // when
        ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> authenticationService.getAuthentication("test:test1")
        );

        // then
        assertThat(validationException.getMessage()).isEqualTo("Check User and password");
    }
}