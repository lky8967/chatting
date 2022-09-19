package com.example.chatting.chat;

import com.example.chatting.chatroom.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;


public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findAllByRoomIdOrderByIdAsc(Long roomId);

    // 메시지 안읽은 갯수 카운트 쿼리
    @Query("SELECT count(msg) FROM ChatMessage msg WHERE msg.senderId =:userId AND msg.roomId =:roomId AND msg.isRead = false")
    int countMsg(Long userId, Long roomId);

    // 채팅 메시지 읽음 상태 일괄 업데이트
    // 이 어노테이션은 @Query 어노테이션으로 작성된 조회를 제외한 데이터에 변경이 일어나는 삽입 , 수정 , 삭제 쿼리 메서드를 사용할때 필요하다.
    // 주로 복잡한 벌크 연산 - 다중 UPDATE , DELETE를 연산을 하나의 쿼리로 수행할때 사용.
    // 추가적인 블로그링크 https://ozofweird.tistory.com/entry/%EC%82%BD%EC%A7%88-%ED%94%BC%ED%95%98%EA%B8%B0-Query-%EC%96%B4%EB%85%B8%ED%85%8C%EC%9D%B4%EC%85%98-%EC%82%AC%EC%9A%A9%EB%B2%95
    @Modifying
    @Transactional
    @Query("UPDATE ChatMessage msg SET msg.isRead = true WHERE msg.roomId = :roomId AND msg.senderId <> :userId AND msg.isRead = false ")
    void updateChatMessage(Long roomId, Long userId);

    // 채팅 메시지 TALK 에서 OUT으로 바꾸기
    @Modifying
    @Transactional
//    @Query("UPDATE ChatMessage msg SET msg.type = 'OUT' WHERE msg.roomId = :roomId AND msg.senderId <> :userId AND msg.type = 'TALK' ")
    @Query("UPDATE ChatMessage SET type = 'OUT' WHERE roomId = :roomId AND senderId = :userId AND type = 'TALK' ")
//    @Query("UPDATE ChatMessage SET type = 'OUT' WHERE type = 'TALK' ")
    void TypeChatMessage(Long roomId, Long userId);

    ChatRoom findByRoomId(Long roomId);
}



