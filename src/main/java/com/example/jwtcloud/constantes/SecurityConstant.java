package com.example.jwtcloud.constantes;

import java.util.Arrays;
import java.util.List;

public class SecurityConstant {

    public static final long TEMPS_EXPIRATION = 432_000_000; // 5 jours exprimé en milliseconds
    //    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_NON_VERIFIE = "Token non vérifié";
    public static final String GET_ARRAYS_LLC = "Alphonse ZIGANI Ingénieur informaticien";
    public static final String GET_ARRAYS_ADMINISTRATION = "Portail de gestion des utilisateurs";
    public static final String AUTHORITIES = "autorisations";
    public static final String FORBIDDEN_MESSAGE = "Une connexion est requise pour accéder à cette page.";
    public static final String ACCESS_DENIED_MESSAGE = "Vous n'avez pas l'autorisation d'accéder à cette page.";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    //Ici je défini les urls public, pas besoin d'authentification
//    public static final String[] URLS_PUBLIC = { "/user/login", "/user/register", "/user/modifierMotDePasse/**", "/user/image/**" };
    public static final String[] URLS_PUBLIC = {"/**"};
//    public static final List<String> URLS_PUBLIC = Arrays.asList("/**");

}
