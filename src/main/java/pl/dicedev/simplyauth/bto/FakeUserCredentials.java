package pl.dicedev.simplyauth.bto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FakeUserCredentials {

    private UUID id;
    private String name;
    private String scope;
    private String password;

}
