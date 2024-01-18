package pl.dicedev.simplyauth.service;

import org.springframework.stereotype.Service;
import pl.dicedev.simplyauth.dto.AuthDto;
import pl.dicedev.simplyauth.dto.UserNameDto;
import pl.dicedev.simplyauth.exception.ValidationException;

import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class AuthenticationService {

    private static final long TOKEN_VALIDITY_IN_MINUTES = 10;
    private static final long SECONDS_IN_MINUTES = 60;
    private static final long TOKEN_VALIDITY_IN_SECONDS = TOKEN_VALIDITY_IN_MINUTES * SECONDS_IN_MINUTES;
    private static final String CREDENTIALS_SEPARATOR = ":";
    private static final String TOKEN_SEPARATOR = ";";


    private Map<String, String> userDataMap = new HashMap<>() {{
        put("test", "test");
        put("gracz1", "gracz1");
    }};
    private Map<String, String> userScopeMap = new HashMap<>() {{
        put("test", "user-test-scope");
        put("gracz1", "user-gracz1-scope");
    }};

    public AuthDto getAuthentication(String credentials) {
        String[] userData = credentials.split(CREDENTIALS_SEPARATOR);
        validCredentials(userData);

        String tokenMap =
                "userName=" + userData[0] +
                        ";validTime=" + Instant.now().plusSeconds(TOKEN_VALIDITY_IN_SECONDS).toString() +
                        ";scope=" + userScopeMap.get(userData[0]);

        String token = Base64.getEncoder().withoutPadding().encodeToString(tokenMap.getBytes());

        return new AuthDto(token);
    }

    public boolean isTokenValid(String token) {
        byte[] userDataByte = Base64.getDecoder().decode(token.getBytes());
        String[] userDataRows = new String(userDataByte).split(TOKEN_SEPARATOR);
        String[] userDataName = userDataRows[0].split("=");
        String[] userDataTime = userDataRows[1].split("=");
        String[] userDataScope = userDataRows[2].split("=");
        if (!userDataMap.containsKey(userDataName[1])) {
            throw new ValidationException("Invalid token");
        }
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
        if (!userDataMap.containsKey(userDataName[1])) {
            throw new ValidationException("Invalid token");
        }

        return new UserNameDto(userDataName[1]);
    }

    private void validCredentials(String[] userData) {
        if (userData.length != 2) {
            throw new ValidationException("User or password are incorrect");
        }
        if (!userDataMap.containsKey(userData[0])) {
            throw new ValidationException("User/password incorrect");
        }
        if (!userDataMap.get(userData[0]).equals(userData[1])) {
            throw new ValidationException("Check User and password");
        }
    }

    public boolean createUser(String username, String password, String scope) {
        userDataMap.put(username, password);
        userScopeMap.put(username, Objects.requireNonNullElse(scope, "user-scope"));

        return true;
    }
}
