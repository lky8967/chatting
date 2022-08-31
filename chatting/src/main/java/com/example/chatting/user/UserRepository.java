package com.example.chatting.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User , Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByNickname(String nickname);
    @Query("SELECT u FROM User u")
    List<User> findAllById();
}
