//package com.example.jwtcloud.controller;
//
//import com.example.jwtcloud.models.User;
//import com.example.jwtcloud.services.IAuthenticationService;
//import com.example.jwtcloud.services.IUserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("api/authentication")//pre-path
//public class AuthenticationController
//{
//    @Autowired
//    private IAuthenticationService authenticationService;
//
//    @Autowired
//    private IUserService userService;
//
//    @PostMapping("senregistrer") //api/authentication/senregistrer
//    public ResponseEntity<?> signUp(@RequestBody User user)
//    {
//        if (userService.findByUsername(user.getUsername()).isPresent())
//        {
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }
//        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
//    }
//
//    @PostMapping("connexion")//api/authentication/connexion
//    public ResponseEntity<?> signIn(@RequestBody User user)
//    {
//        return new ResponseEntity<>(authenticationService.signInAndReturnJWT(user), HttpStatus.OK);
//    }
//}
