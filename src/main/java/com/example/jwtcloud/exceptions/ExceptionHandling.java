package com.example.jwtcloud.exceptions;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.jwtcloud.constantes.SecurityConstant;
import com.example.jwtcloud.exceptions.domain.*;
import com.example.jwtcloud.models.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.Objects;


@RestControllerAdvice
public class ExceptionHandling implements  ErrorController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private static final String COMPTE_VERROUILLE = "Votre compte a été verrouillé. Veuillez contacter l'administrateur";
    private static final String METHODE_NON_AUTORISEE = "Cette méthode de requête n'est pas autorisée sur ce point de terminaison. Veuillez envoyer une requête '%s'";
    private static final String ERREUR_SERVEUR_INTERNE = "Une erreur s'est produite lors du traitement de la requête";
    private static final String IDENTIFIANTS_INCORRECTS = "Nom d'utilisateur / mot de passe incorrect ." +"\n\n\n\n"+
                                                                "Veuillez réessayer!";
    private static final String COMPTE_DESACTIVE = "Votre compte a été désactivé. Si c'est une erreur, veuillez contacter l'administrateur";
    private static final String ERREUR_TRAITEMENT_FICHIER = "Une erreur s'est produite lors du traitement du fichier";
    private static final String PERMISSION_INSUFFISANTE = "Vous n'avez pas suffisamment de permissions ou autorisations";
    private static final String PAS_URL_ERREUR404 = "Erreur 404 pas d\'URL pour ce chemin";
    public static final String CHEMIN_ERREUR = "/erreur";


    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus,
                httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> accountDisabledException() {
        return createHttpResponse(HttpStatus.BAD_REQUEST, COMPTE_DESACTIVE);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException() {
        return createHttpResponse(HttpStatus.BAD_REQUEST, IDENTIFIANTS_INCORRECTS);
    }


    

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException() {
        return createHttpResponse(HttpStatus.FORBIDDEN, PERMISSION_INSUFFISANTE);
    }




    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> lockedException() {
        return createHttpResponse(HttpStatus.UNAUTHORIZED, COMPTE_VERROUILLE);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException exception) {
        return createHttpResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<HttpResponse> emailExistException(EmailExistException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UsernameExistException.class)
    public ResponseEntity<HttpResponse> usernameExistException(UsernameExistException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
//Pour les urls non trouvés
//
//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ResponseEntity<HttpResponse> noHandlerFoundException(NoHandlerFoundException e) {
//        return createHttpResponse(HttpStatus.BAD_REQUEST, SecurityConstant.PAS_URL_EXISTANT);
//    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED, String.format(METHODE_NON_AUTORISEE, supportedMethod));
//        return createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED, String.format(SecurityConstant., supportedMethod));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERREUR_SERVEUR_INTERNE);
    }

    @ExceptionHandler(NotAnImageFileException.class)
    public ResponseEntity<HttpResponse> notAnImageFileException(NotAnImageFileException exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<HttpResponse> notFoundException(NoResultException exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<HttpResponse> iOException(IOException exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERREUR_TRAITEMENT_FICHIER);
    }



    @RequestMapping(CHEMIN_ERREUR)
    public ResponseEntity<HttpResponse> nontrouve404() {
        return createHttpResponse(HttpStatus.NOT_FOUND, PAS_URL_ERREUR404);
    }


    public String getErrorPath(){
        return  CHEMIN_ERREUR;
    }



}
