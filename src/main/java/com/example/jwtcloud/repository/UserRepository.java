package com.example.jwtcloud.repository;


import com.example.jwtcloud.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Query(value = "select * from user where username =:username", nativeQuery = true)
  User findUserByUsername(String username);

  @Query(value = "select * from user where email =:email", nativeQuery = true)
  User findUserByByEmail(String email);

  @Query(value = "select * from user where userId =:userId", nativeQuery = true)
  User findUserById(long userId);



}
