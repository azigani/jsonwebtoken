//package com.example.jwtcloud.Jwtutils;
//
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.util.StringUtils;
//
//import javax.servlet.http.HttpServletRequest;
//
//public class SecurityUtils {
//
//    public static final String ROLE_PREFIX = "ROLE_";
//    public static final String AUTH_HEADER = "authorization";
//    public static final String AUTH_TOKEN_TYPE = "Bearer";
//    public static final String AUTH_TOKEN_PREFIX = AUTH_TOKEN_TYPE + " ";
//
//
//    public static SimpleGrantedAuthority convertToAuthority(String role) {
//        String roleFormater = role.startsWith(ROLE_PREFIX) ? role : ROLE_PREFIX + role;
//        return new SimpleGrantedAuthority(roleFormater);
//    }
//
//    public static String extrairetAuthenticationTokenFromrequest(HttpServletRequest httpServletRequest) {
//
//        String bearerToken = httpServletRequest.getHeader(AUTH_HEADER);
//        if (!StringUtils.hasLength(bearerToken) && bearerToken.startsWith(AUTH_TOKEN_PREFIX)) {
//            return bearerToken.substring(7);
//        }
//        return null;
//
//    }
//}
