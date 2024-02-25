package com.example.jwtcloud.constantes;

import java.util.Arrays;
import java.util.List;

public class SecurityConstant {

    public static final long TEMPS_EXPIRATION = 604_800_000*2; // 5 jours exprimé en milliseconds*2 donc 10jr
    //    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_NON_VERIFIE = "Token non vérifié";
    public static final String GET_ARRAYS_LLC = "Alphonse ZIGANI Ingénieur informaticien";
    public static final String GET_ARRAYS_ADMINISTRATION = "Portail de gestion des utilisateurs";
    public static final String AUTHORITIES = "autorisations";
    public static final String FORBIDDEN_MESSAGE = "Une connexion est requise pour accéder à cette page.";
    public static final String ACCESS_DENIED_MESSAGE = "Vous n'avez pas l'autorisation d'accéder à cette page.";
    public static final String PAS_URL_EXISTANT = "Pas de chemin de mapping vers cet URL ou cette page est non trouvable";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
//    public static final String[] URLS_PUBLIC = {"/**"};
    public static final String[] URLS_PUBLIC = {"/user/connexion","/user/senregistrer"};
//    public static final String[] URLS_PUBLIC = {"/**"};

}
