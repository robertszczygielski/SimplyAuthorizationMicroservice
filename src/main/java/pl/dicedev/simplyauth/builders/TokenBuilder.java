package pl.dicedev.simplyauth.builders;

import java.time.Instant;

public class TokenBuilder {

    private String userName;
    private String validTime;
    private String scope;
    private String id;
    private String tokenSeparator;

    public TokenBuilder(String tokenSeparator) {
        this.tokenSeparator = tokenSeparator;
    }

    public static TokenBuilder builder(String tokenSeparator) {
        return new TokenBuilder(tokenSeparator);
    }

    public TokenBuilder withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public TokenBuilder withValidTime(Long validTime) {
        this.validTime = Instant.now().plusSeconds(validTime).toString();
        return this;
    }

    public TokenBuilder withScope(String scope) {
        this.scope = scope;
        return this;
    }

    public TokenBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public String build() {
        return "userName=" + userName + tokenSeparator +
                "validTime=" + validTime + tokenSeparator +
                "scope=" + scope + tokenSeparator +
                "id=" + id;
    }
}
