package com.example.jwtcloud.constantes;

public class Permissions {

    public static final String[] USER_AUTHORITIES = {"user:lire"};
    public static final String[] CAISSIER_ENREGISTRER = {"user:lire","user:creer"};
    public static final String[] CAISSIER_VALIDER = {"user:lire","user:valider"};
    public static final String[] HR_AUTHORITIES = {"user:lire", "user:miseAJour", "user:verifier"};
    public static final String[] MANAGER_AUTHORITIES = {"user:lire", "user:miseAJour", "user:verifier"};
    public static final String[] ADMIN_AUTHORITIES = {"user:lire", "user:creer", "user:miseAJour", "user:verifier","user:supprimer"};
    public static final String[] SUPER_ADMIN_AUTHORITIES = {"user:lire", "user:creer", "user:miseAJour", "user:supprimer", "user:verifier"};
    public static final String[] USER_VERIFIER = {"user:lire", "user:verifier"};
    public static final String[] ADMIN_VERIFICATEUR = {"user:lire", "user:verifier"};
    public static final String[] USER_VALIDER = {"user:lire", "user:valider"};
    public static final String[] ADMIN_VALIDER = {"user:lire", "user:valider"};
    public static final String[] TRANSMETTRE_SUPERIEUR = {"user:lire", "user:verifier", "user:transmettre"};


}
