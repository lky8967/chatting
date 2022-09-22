package com.example.chatting.chatroom.banned;

import com.example.chatting.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BannedRepository extends JpaRepository<BannedUser, Long> {

    Optional<BannedUser> findByUserAndBannedUser(User user, User bannedUser);

//    @Query(value = "SELECT banned_user FROM banned_user WHERE banned_id  " , nativeQuery = true)
//    List<BannedUser> findAllMyBannedByUser(User user);
    List<BannedUser> findAllMyBannedByUser(User user);


//    Boolean existsByUser(User acceptor, User requester);
//    Boolean existsById(Long accId, Long reqId);
//    Boolean existsByBannedUserAndUser(Long accId, Long reqId);

//    Boolean findByIsBannedAndBannedUserAndUser(Long accId, Long reqId);
    Boolean existsByBannedUserIdAndUser(Long userId, User BannedUser);

}
