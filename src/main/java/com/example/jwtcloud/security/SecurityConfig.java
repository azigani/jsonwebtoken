//package com.example.jwtcloud.security;
//import com.example.jwtcloud.models.Role;
//import com.example.jwtcloud.security.jwt.InternalApiAuthenticationFilter;
//import com.example.jwtcloud.security.jwt.JwtAuthorizationFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.BeanIds;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter
//{
//    @Value("${authentication.internal-api-key}")
//    private String internalApiKey;
//
//    @Autowired
//    private CustomerDetailsService userDetailsService;
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception
//    {
//        auth.userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder());
//    }
//
//    @Override
//    @Bean(BeanIds.AUTHENTICATION_MANAGER)
//    public AuthenticationManager authenticationManagerBean() throws Exception
//    {
//        return super.authenticationManagerBean();
//    }
//
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception
//    {
//        http.cors();
//        /**
//         * Desactiver les csrf
//         */
//        http.csrf().disable();
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        http.authorizeRequests()
//                .antMatchers("/api/authentication/**").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/book").permitAll()
////                .antMatchers("/api/book/**").hasRole(Role.ADMIN.name())
//                //accessible uniquement par le manager systeme
//  .antMatchers("/api/internal/**").hasRole(Role.SYSTEM_MANAGER.name())
//                .anyRequest().authenticated();
//
//        //jwt filter
//        //internal > jwt > authentication
//
//        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(internalApiAuthenticationFilter(), JwtAuthorizationFilter.class);
//    }
//
////creation du bean de notre filter
//    @Bean
//    public InternalApiAuthenticationFilter internalApiAuthenticationFilter()
//    {
//        return new InternalApiAuthenticationFilter(internalApiKey);
//    }
//
//    @Bean
//    public JwtAuthorizationFilter jwtAuthorizationFilter()
//    {
//        return new JwtAuthorizationFilter();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder()
//    {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer()
//    {
//        return new WebMvcConfigurer()
//        {
//            @Override
//            public void addCorsMappings(CorsRegistry registry)
//            {
//                registry.addMapping("/**")
//                        .allowedOrigins("*")
//                        .allowedMethods("*");
//            }
//        };
//    }
//}
