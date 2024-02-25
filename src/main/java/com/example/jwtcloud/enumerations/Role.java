package com.example.jwtcloud.enumerations;

import static com.example.jwtcloud.constantes.Permissions.*;

public enum Role {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_HR(HR_AUTHORITIES),
    ROLE_MANAGER(MANAGER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES),
    ROLE_SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES),
    ROLE_USER_VERIFICATEUR(USER_VERIFIER),
    ROLE_ADMIN_VERIFICATEUR(ADMIN_VERIFICATEUR),
    ROLE_USER_VALIDER(USER_VALIDER),
    ROLE_ADMIN_VALIDER(ADMIN_VALIDER),
    ROLE_TRANSMETTRE_SUPERIEUR(TRANSMETTRE_SUPERIEUR),
    ROLE_CAISSIER_VALIDER(CAISSIER_VALIDER),
    ROLE_CAISSIER(CAISSIER_ENREGISTRER);

    private String[] autorisations;

    Role(String... autorisations) {
        this.autorisations = autorisations;
    }

    public String[] getAuthorities() {
        return autorisations;
    }


}
