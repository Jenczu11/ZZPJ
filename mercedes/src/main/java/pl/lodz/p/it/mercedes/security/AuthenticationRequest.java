package pl.lodz.p.it.mercedes.security;

import lombok.Data;

import java.io.Serializable;

public @Data class AuthenticationRequest implements Serializable {

    private String username;
    private String password;
}
