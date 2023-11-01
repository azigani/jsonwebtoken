//package com.example.demo.repository;
//
//
//import com.example.demo.models.Role;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//public interface RoleRepository extends JpaRepository<Role, String> {
//
//    @Query(value = "SELECT * FROM roles WHERE roleName =:roleName", nativeQuery = true)
//    Role findByRoleName(String roleName);
//
//}
