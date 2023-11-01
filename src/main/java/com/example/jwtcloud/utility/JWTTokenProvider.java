package com.example.jwtcloud.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.jwtcloud.models.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static java.util.Arrays.*;
import static com.example.jwtcloud.constantes.SecurityConstant.*;
import org.apache.commons.lang3.StringUtils;

@Component
public class JWTTokenProvider {

//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.secret}")
//    private String secret;
//
//    public String genererJwtToken(UserPrincipal userPrincipal) {
//        String[] claims = getClaimsFromUser(userPrincipal);
//        return JWT.create().withIssuer(GET_ARRAYS_LLC).withAudience(GET_ARRAYS_ADMINISTRATION)
//                .withIssuedAt(new Date()).withSubject(userPrincipal.getUsername())
//                .withArrayClaim(AUTHORITIES, claims).withExpiresAt(new Date(System.currentTimeMillis() + TEMPS_EXPIRATION))
//                .sign(HMAC512(secret.getBytes()));
//
//    }
//
//    public List<GrantedAuthority> getAutorisations(String token) {
//        String[] claims = getClaimsFromToken(token);
//        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//    }
//
//    public Authentication getAuthentication(String username, List<GrantedAuthority> autorisations, HttpServletRequest request) {
//        UsernamePasswordAuthenticationToken userPasswordAuthToken = new
//                UsernamePasswordAuthenticationToken(username, null, autorisations);
//        userPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        return userPasswordAuthToken;
//    }
//
//    public boolean isTokenValid(String username, String token) {
//        JWTVerifier verifier = getJWTVerifier();
//        return StringUtils.isNotEmpty(username) && !isTokenExpired(verifier, token);
//    }
//
//    public String getSubject(String token) {
//        System.out.println("token " +token);
//        JWTVerifier verifier = getJWTVerifier();
//        return verifier.verify(token).getSubject();
//    }
//
//
//
//    private boolean isTokenExpired(JWTVerifier verifier, String token) {
//        Date expiration = verifier.verify(token).getExpiresAt();
//        return expiration.before(new Date());
//
//    }
//
//
//    private String[] getClaimsFromToken(String token) {
//        JWTVerifier verifier = getJWTVerifier();
//        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
//    }
//
//    private JWTVerifier getJWTVerifier() {
//        JWTVerifier verifier;
//        try {
//            Algorithm algorithm = HMAC512(secret);
//            verifier = JWT.require(algorithm).withIssuer(GET_ARRAYS_LLC).build();
//        }catch (JWTVerificationException exception) {
//            throw new JWTVerificationException(TOKEN_NON_VERIFIE);
//        }
//        return verifier;
//    }
//
//    private String[] getClaimsFromUser(UserPrincipal user) {
//        List<String> autorisations = new ArrayList<>();
//        for (GrantedAuthority grantedAuthority : user.getAuthorities()){
//            autorisations.add(grantedAuthority.getAuthority());
//        }
//        return autorisations.toArray(new String[0]);
//    }


    @Value("${jwt.secret}")
    private String secret;

    public String genererJwtToken(UserPrincipal userPrincipal) {
        Algorithm algorithm = Algorithm.HMAC512(secret.getBytes());
        String[] claims = getClaimsFromUser(userPrincipal);
        return JWT.create()
                .withIssuer(GET_ARRAYS_LLC)
                .withAudience(GET_ARRAYS_ADMINISTRATION)
                .withIssuedAt(new Date())
                .withSubject(userPrincipal.getUsername())
                .withArrayClaim(AUTHORITIES, claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + TEMPS_EXPIRATION))
                .sign(algorithm);
    }

    public Authentication getAuthentication(String user, List<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthToken =
                new UsernamePasswordAuthenticationToken(user, null, authorities);
        usernamePasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return usernamePasswordAuthToken;
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        String[] claims = getClaimsFromToken(token);
        return Arrays.stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public String getSubject(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getSubject();
    }

    public boolean isTokenValid(String username, String token) {
        JWTVerifier verifier = getJWTVerifier();
        return StringUtils.isNotEmpty(username) && !isTokenExpired(verifier, token);
    }

    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expiredDate = verifier.verify(token).getExpiresAt();
        return expiredDate.before(new Date());
    }

    private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
        List<String> authorities = new ArrayList<>();
        userPrincipal.getAuthorities().forEach(authority -> authorities.add(authority.getAuthority()));
        return authorities.toArray(new String[0]);
    }

    private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            verifier = JWT.require(algorithm).withIssuer(GET_ARRAYS_LLC).build();
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException(TOKEN_NON_VERIFIE);
        }
        return verifier;
    }


}
