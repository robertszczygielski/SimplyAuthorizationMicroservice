package pl.dicedev.simplyauth.service;

import org.springframework.stereotype.Service;
import pl.dicedev.simplyauth.bto.FakeUserCredentials;
import pl.dicedev.simplyauth.dto.AuthDto;
import pl.dicedev.simplyauth.dto.UserIdDto;
import pl.dicedev.simplyauth.dto.UserNameDto;
import pl.dicedev.simplyauth.exception.ValidationException;

import java.time.Instant;
import java.util.*;

@Service
public class AuthenticationService {

    private static final long TOKEN_VALIDITY_IN_MINUTES = 10;
    private static final long SECONDS_IN_MINUTES = 60;
    private static final long TOKEN_VALIDITY_IN_SECONDS = TOKEN_VALIDITY_IN_MINUTES * SECONDS_IN_MINUTES;
    private static final String CREDENTIALS_SEPARATOR = ":";
    private static final String TOKEN_SEPARATOR = ";";

    private List<FakeUserCredentials> users = new ArrayList<>() {{
        add(FakeUserCredentials.builder()
                .id(UUID.fromString("53346916-54d4-4ce8-a539-b6813a47f6f5"))
                .name("test")
                .password("test")
                .scope("user-test-scope")
                .build());
        add(FakeUserCredentials.builder()
                .id(UUID.fromString("e1a84ff3-add8-4c71-8990-f92896587658"))
                .name("gracz1")
                .password("gracz1")
                .scope("user-gracz1-scope")
                .build());
    }};

    public AuthDto getAuthentication(String credentials) {
        String[] userData = credentials.split(CREDENTIALS_SEPARATOR);
        validCredentials(userData);

        FakeUserCredentials userCredentials = getUserName(userData[0]);

        String tokenMap =
                "userName=" + userCredentials.getName() +
                        ";validTime=" + Instant.now().plusSeconds(TOKEN_VALIDITY_IN_SECONDS).toString() +
                        ";scope=" + userCredentials.getScope() +
                        ";id=" + userCredentials.getId();

        String token = Base64.getEncoder().withoutPadding().encodeToString(tokenMap.getBytes());

        return new AuthDto(token);
    }

    public boolean isTokenValid(String token) {
        byte[] userDataByte = Base64.getDecoder().decode(token.getBytes());
        String[] userDataRows = new String(userDataByte).split(TOKEN_SEPARATOR);
        String[] userDataName = userDataRows[0].split("=");
        String[] userDataTime = userDataRows[1].split("=");
        String[] userDataScope = userDataRows[2].split("=");

        getUserName(userDataName[1]);

        Instant date = Instant.parse(userDataTime[1]);
        if (Instant.now().isAfter(date)) {
            throw new ValidationException("Token Expired");
        }
        return true;
    }

    public UserNameDto getName(String token) {
        byte[] userDataByte = Base64.getDecoder().decode(token.getBytes());
        String[] userDataRows = new String(userDataByte).split(TOKEN_SEPARATOR);
        String[] userDataName = userDataRows[0].split("=");

        getUserName(userDataName[1]);

        return new UserNameDto(userDataName[1]);
    }

    private void validCredentials(String[] userData) {
        if (userData.length != 2) {
            throw new ValidationException("User or password are incorrect");
        }

        FakeUserCredentials userCredentials = getUserName(userData[0]);

        if (!userCredentials.getPassword().equals(userData[1])) {
            throw new ValidationException("Check User and password");
        }
    }

    public boolean createUser(String id, String username, String password, String scope) {
        users.add(
                FakeUserCredentials.builder()
                        .id(UUID.fromString(id))
                        .scope(Objects.requireNonNullElse(scope, "user-scope"))
                        .name(username)
                        .password(password)
                        .build()
        );

        return true;
    }

    public UserIdDto getId(String token) {
        byte[] userDataByte = Base64.getDecoder().decode(token.getBytes());
        String[] userDataRows = new String(userDataByte).split(TOKEN_SEPARATOR);
        String[] userDataId = userDataRows[3].split("=");

        return new UserIdDto(userDataId[1]);
    }

    private FakeUserCredentials getUserName(String username) {

        Optional<FakeUserCredentials> userCredentialsOptional = users.stream()
                .filter(u -> u.getName().equalsIgnoreCase(username))
                .findFirst();

        if (userCredentialsOptional.isEmpty()) {
            throw new ValidationException("User/password incorrect");
        }

        return userCredentialsOptional.get();
    }
}
