package com.example.jwtcloud.config;


import com.example.jwtcloud.filter.JwtAccessDeniedHandler;
import com.example.jwtcloud.filter.JwtAuthenticationEntryPoint;
import com.example.jwtcloud.filter.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.jwtcloud.constantes.SecurityConstant.*;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MaConfigSecurite extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Qualifier("userDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        /**Configuration le gestionnaire d'authentification pour utiliser le service UserDetails*/
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        /**
         * tilisons  BCryptPasswordEncoder pour encoder les mots de passe
         */
        // U
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public MaConfigSecurite() {

    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().
//                disable()
//                .cors()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and().authorizeRequests()
//                .antMatchers(URLS_PUBLIC)
//                .permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .exceptionHandling().
//                accessDeniedHandler(jwtAccessDeniedHandler)
//                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                .and()
//                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
//    }
//


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        /**Désactivons la vérification CSRF
         *
         */
        httpSecurity.csrf().disable()
                /**Autorisons certaines requêtes sans authentification
                 *
                 */

                .authorizeRequests().antMatchers(URLS_PUBLIC).permitAll()
                /** Toutes les autres requêtes nécessitent une authentification
                 *
                 */
                .anyRequest().authenticated()
                .and()
                /**Configuration du gestionnaire d'authentification JWT
                 *
                 */
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        /** Ajoutons un filtre JWT personnalisé avant UsernamePasswordAuthenticationFilter
         *
         */
        httpSecurity.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }


}
