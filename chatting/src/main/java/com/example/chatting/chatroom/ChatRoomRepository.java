package com.example.chatting.chatroom;


import com.example.chatting.user.User;
import com.example.chatting.chatroom.RoomDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 유저를 찾아오면서, 동시에 방의 메시지 안읽음 카운트까지 계산하여 가져와야 합니다.
    @Query(value =  "SELECT DISTINCT r.room_id AS roomId " +
                                    ", r.acc_out AS accOut " +
                                    ", r.req_out AS reqOut " +
                                    ", r.acc_fixed AS accFixed " +
                                    ", r.req_fixed AS reqFixed " +
                                    ", u1.user_id AS accId " +
                                    ", u1.nickname AS accNickname " +
                                    ", u2.user_id AS reqId " +
                                    ", u2.nickname AS reqNickname " +
                                    ", msg.message AS message " +
                                    ", msg.created_at AS date " +
                                    ", u1.user_img_url AS acceptorUserImgUrl " +
                                    ", u2.user_img_url AS requesterUserImgUrl " +
                    "FROM chat_room r " +
                    "INNER JOIN users u1 ON r.acceptor_user_id = u1.user_id " +
                    "INNER JOIN users u2 ON r.requester_user_id = u2.user_id " +
                    "INNER JOIN chat_message msg ON (msg.room_id, msg.message_id) " +
                    "IN (SELECT room_id, MAX(message_id) FROM chat_message GROUP BY room_id) AND r.room_id = msg.room_id " +
                    "WHERE (r.acceptor_user_id = :user OR r.requester_user_id = :user) " +
                    "ORDER BY r.modify_at DESC",
            nativeQuery = true)
    List<RoomDto> findAllWith(@Param("user") User user);

    @Query("SELECT room FROM ChatRoom room JOIN FETCH room.acceptor JOIN FETCH room.requester WHERE room.id = :roomId")
    Optional<ChatRoom> findByIdFetch(Long roomId);

    // 채팅방 찾아오기
    @Query("SELECT room FROM ChatRoom room " +
            "WHERE (room.requester = :requester AND room.acceptor = :acceptor) OR " +
            "(room.requester = :acceptor AND room.acceptor = :requester)")
    Optional<ChatRoom> findByUser(@Param("requester") User requester, @Param("acceptor") User acceptor);

}
