package com.example.chatting.chatroom.banned;

import com.example.chatting.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BannedRepository extends JpaRepository<BannedUser, Long> {

    Optional<BannedUser> findByUserAndBannedUser(User user, User bannedUser);

    List<BannedUser> findAllMyBannedByUser(User user);
    boolean existsByBannedUserAndUser(User acceptor, User requester);
    Boolean existsByBannedUserIdAndUser(Long userId, User BannedUser);

    boolean existsByUserAndBannedUser(User user , User BannedUser);


}
