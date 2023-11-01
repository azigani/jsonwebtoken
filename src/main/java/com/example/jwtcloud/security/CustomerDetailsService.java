//package com.example.jwtcloud.security;
//
//import com.example.jwtcloud.Jwtutils.SecurityUtils;
//import com.example.jwtcloud.models.User;
//import com.example.jwtcloud.services.IUserService;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Service
//public class CustomerDetailsService implements UserDetailsService
//{
//    IUserService userService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userService.findByUsername(username)
//                .orElseThrow(()-> new UsernameNotFoundException(username));
////        Set<GrantedAuthority> autorisations =  Set.of(SecurityUtils.convertToAuthority(user.getRole().name()));
//
//        Set<GrantedAuthority> autorisations = new HashSet<>();
//        autorisations.add(SecurityUtils.convertToAuthority(user.getRole().name()));
//        return null;
//
//    }
//}
