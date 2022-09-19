package com.example.chatting.chatroom.banned;

import com.example.chatting.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BannedRepository extends JpaRepository<BannedUser, Long> {

    Optional<BannedUser> findByUserAndBannedUser(User user, User bannedUser);
    List<User> findAllMyBannedByUser(User user);

//    void deleteById(User user, User bannedUser);

//    void delete(User user, User bannedUser);
}
