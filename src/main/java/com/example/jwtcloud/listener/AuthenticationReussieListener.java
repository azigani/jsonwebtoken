package com.example.jwtcloud.listener;

import com.example.jwtcloud.models.UserPrincipal;
import com.example.jwtcloud.services.servicesImpl.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationReussieListener {
    @Autowired
    private LoginAttemptService loginAttemptService;

    @EventListener
    public void onAuthenticationReussie(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if(principal instanceof UserPrincipal) {
            UserPrincipal user = (UserPrincipal) event.getAuthentication().getPrincipal();
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

}
