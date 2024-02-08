package pl.dicedev.simplyauth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.dicedev.simplyauth.dto.*;
import pl.dicedev.simplyauth.service.AuthenticationService;

@RestController
@RequestMapping("/v1/authentication")
@AllArgsConstructor
public class AuthenticationController {
    private AuthenticationService authenticationService;

    @GetMapping("/{credentials}")
    @Operation(description = "Example string as credentials: 'test:test', the ':' separates username and password")
    public AuthDto getAuthenticationPathVariable(@PathVariable String credentials) {
        return authenticationService.getAuthentication(credentials);
    }

    @GetMapping("/credentials")
    public AuthDto getAuthenticationRequestParam(@RequestParam String credentials) {
        return authenticationService.getAuthentication(credentials);
    }

    @GetMapping("/credentials/headers")
    public AuthDto getAuthenticationRequestHeader(@RequestHeader("credentials") String credentials) {
        return authenticationService.getAuthentication(credentials);
    }

    @PostMapping("/credentials/body")
    public AuthDto getAuthenticationRequestBody(@RequestBody AuthUserDto authUserDto) {
        return authenticationService.getAuthentication(
                authUserDto.getUsername() + ":" + authUserDto.getPassword()
        );
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

    @GetMapping(value = "/scope/{token}")
    public UserScopeDto getUserScope(@PathVariable String token) {
        return authenticationService.getUserScope(token);
    }

    @GetMapping(value = "/scope")
    public UserScopeDto getUserScopeRH(@RequestHeader("authorization") String authorization) {
        String removeBearer = authorization.split(" ")[1];
        return authenticationService.getUserScope(removeBearer);
    }

}
