package com.example.chatting.chatroom.banned;

import com.example.chatting.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BannedRepository extends JpaRepository<BannedUser, Long> {

    Optional<BannedUser> findByUserAndBannedUser(User user, User bannedUser);

//    @Query(value = "SELECT banned_user FROM banned_user WHERE banned_id  " , nativeQuery = true)
    List<BannedUser> findAllMyBannedByUser(User user);

//    List<User> findAllById(User user);
//
//    List<User> findAllByBannedId(User user);

//    void deleteById(User user, User bannedUser);

//    void delete(User user, User bannedUser);
}
