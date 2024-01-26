package pl.dicedev.simplyauth.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.dicedev.simplyauth.dto.AuthDto;
import pl.dicedev.simplyauth.dto.UserIdDto;
import pl.dicedev.simplyauth.dto.UserNameDto;
import pl.dicedev.simplyauth.service.AuthenticationService;

@RestController
@RequestMapping("/v1/authentication")
@AllArgsConstructor
public class AuthenticationController {
    private AuthenticationService authenticationService;

    @GetMapping("/{credentials}")
    public AuthDto getAuthentication(@PathVariable String credentials) {
        return authenticationService.getAuthentication(credentials);
    }

    @GetMapping("/validate/{token}")
    public boolean getValidate(@PathVariable String token) {
        return authenticationService.isTokenValid(token);
    }

    @GetMapping(value = "/name/{token}")
    public UserNameDto getNameFormToken(@PathVariable String token) {
        return authenticationService.getName(token);
    }

    @GetMapping(value = "/id/{token}")
    public UserIdDto getIdFormToken(@PathVariable String token) {
        return authenticationService.getId(token);
    }

    @PostMapping("/{uuid}/{username}/{password}")
    public boolean postUser(
            @PathVariable String uuid,
            @PathVariable String username,
            @PathVariable String password,
            @RequestParam(required = false) String scope
    ) {
        return authenticationService.createUser(uuid, username, password, scope);
    }

}
