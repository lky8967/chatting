package com.example.chatting.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User , Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByNickname(String nickname);
    @Query(value = "SELECT * FROM users WHERE user_id order by rand() limit 0, 5" , nativeQuery = true)
//    @Query("SELECT u FROM User u")
    List<User> findAllById();
}
