package com.example.chatting.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User , Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByNickname(String nickname);
    @Query(value = "SELECT * FROM users WHERE user_id != :userId order by rand() limit 0, 5" , nativeQuery = true)
    List<User> findAllById(Long userId);

    @Modifying
    @Query("UPDATE User user SET user.userImgUrl = '' WHERE user.id = :userId ")
    void deleteImg(Long userId);

    @Modifying
    @Query("UPDATE User user SET user.userImgUrl = :userImgUrl ")
    void updateImg(String userImgUrl);
}
