package com.example.jwtcloud.listener;

import com.example.jwtcloud.services.servicesImpl.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener {

    @Autowired
    private LoginAttemptService loginAttemptService;

//methode listner de tentatives

    @EventListener
    public void onAuthentificationEchoue(AuthenticationFailureBadCredentialsEvent event) {

        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof String) {
            String username = (String) event.getAuthentication().getPrincipal();
            loginAttemptService.addUserToLoginAttemptCache(username);

        }

    }
}
