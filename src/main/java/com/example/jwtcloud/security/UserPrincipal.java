//package com.example.jwtcloud.security;
//
//import com.example.jwtcloud.Jwtutils.SecurityUtils;
//import com.example.jwtcloud.models.Role;
//import com.example.jwtcloud.models.User;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//
//
//public class UserPrincipal implements UserDetails {
//    private Long id;
//    private String username;
//    transient private String password; //don't show up on an searialized places
//
//    transient
//    private User user; //user for only login operation, don't use in JWT.
//
//    private Set<GrantedAuthority> autorisations;
//
//    public UserPrincipal(Long id, String username, Set<GrantedAuthority> autorisations) {
//        this.id = id;
//        this.username = username;
//        this.autorisations = autorisations;
//    }
//
//
//    public static UserPrincipal createSuperUser() {
//
//        Set<GrantedAuthority> autorisations = new HashSet<>();
//        autorisations.add(SecurityUtils.convertToAuthority(Role.SYSTEM_MANAGER.name()));
//
//// Crée un objet UserPrincipal manuellement
//        UserPrincipal userPrincipal =
//                new UserPrincipal(-1L,
//                        "manager-systeme",
//                        autorisations);
//
//        return userPrincipal;
//
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return autorisations;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public Set<GrantedAuthority> getAutorisations() {
//        return autorisations;
//    }
//
//    public void setAutorisations(Set<GrantedAuthority> autorisations) {
//        this.autorisations = autorisations;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
