package com.example.jwtcloud.filter;

import com.example.jwtcloud.services.servicesImpl.CustomUserDetailsService;
import com.example.jwtcloud.utility.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import static com.example.jwtcloud.constantes.SecurityConstant.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

//@Autowired
//private JWTTokenProvider jwtTokenProvider;
//
//    public JwtAuthorizationFilter(JWTTokenProvider jwtTokenProvider) {
//        this.jwtTokenProvider = jwtTokenProvider;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        if(request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)){
//            response.setStatus(HttpStatus.OK.value());
//        }else{
//            String autorisationEntete = request.getHeader(HttpHeaders.AUTHORIZATION) ;
//                if(autorisationEntete==null || !autorisationEntete.startsWith(TOKEN_PREFIX)){
//                    filterChain.doFilter(request, response);
//                    return ;
//
//                }
//
//                String token  = autorisationEntete.substring(TOKEN_PREFIX.length());
//
//                String username = jwtTokenProvider.getSubject(token);
//
//                //If token valid
//
//            if(jwtTokenProvider.isTokenValid(username, token) && SecurityContextHolder.getContext()
//                    .getAuthentication()==null){
//                List<GrantedAuthority> autorisations = jwtTokenProvider.getAuthorities(token);
//
//                Authentication authentication = jwtTokenProvider.getAuthentication(username,autorisations,request);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            } else{
//                SecurityContextHolder.clearContext();
//            }
//            }
//
//        filterChain.doFilter(request,response);
//        }

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
          final   String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)) {
            response.setStatus(OK.value());
        } else
            {


            System.out.println("authorizationHeader"  + authorizationHeader);

            if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
                System.out.println("Je rentre si  aauthorizationHeader == null ou !authorizationHeader.startsWith(TOKEN_PREFIX) "  + authorizationHeader);

                filterChain.doFilter(request, response);
                return;
            }
//            System.out.println("else authorizationHeader"  + authorizationHeader);
//            String token = authorizationHeader.substring(TOKEN_PREFIX.length());
//            String token = Optional.ofNullable(authorizationHeader.substring(TOKEN_PREFIX.length())).orElse(String.valueOf(0));
            String token = authorizationHeader.substring(TOKEN_PREFIX.length());

            System.out.println("token prefix  " + TOKEN_PREFIX);
            System.out.println("token token   " + token);
            String username = jwtTokenProvider.getSubject(token);
//            String username = Optional.ofNullable(jwtTokenProvider.getSubject(token)).orElse(String.valueOf(0));

//            String username = Optional.ofNullable(jwtTokenProvider.getSubject(token))
//                    .orElse(String.valueOf(0));

//            String username = jwtTokenProvider.getSubject(token);
            System.out.println("token username   " +username);
            if (jwtTokenProvider.isTokenValid(username, token)) {
//                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                List<GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(token);
                Authentication authentication = jwtTokenProvider.getAuthentication(username, authorities, request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        }
    }
}
