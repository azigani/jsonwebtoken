package com.example.jwtcloud.enumerations;

import static com.example.jwtcloud.constantes.Permissions.*;

public enum Role {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_HR(HR_AUTHORITIES),
    ROLE_MANAGER(MANAGER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES),
    ROLE_SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES),
    ROLE_VERIFICATEUR(USER_VERIFIER);

    private String[] autorisations;

    Role(String... autorisations) {
        this.autorisations = autorisations;
    }

    public String[] getAuthorities() {
        return autorisations;
    }
}
