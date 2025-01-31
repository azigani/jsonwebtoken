package com.example.jwtcloud;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.File;

import static com.example.jwtcloud.constantes.FileConstant.DOSSIER_UTILISATEUR;


@SpringBootApplication
public class JwtCloudApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(JwtCloudApplication.class, args);

        new File(DOSSIER_UTILISATEUR).mkdirs();


    }


//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.setAllowedOrigins(Arrays.asList("${application.serveur-app}"));
//        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
//                "Accept","Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
//                "Access-Control-Request-Method", "Access-Control-Request-Headers"));
//        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept","Jwt-Token" ,"Authorization",
//                "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
//        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//        return new CorsFilter(urlBasedCorsConfigurationSource);
//    }





    @Override
    public void run(String... args) throws Exception {

        System.out.println("Debut enregistrement!!!!");

    }

//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean("myBCryptPasswordEncoder")
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

}
