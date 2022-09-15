package com.example.chatting.chat;

import com.example.chatting.user.User;
import com.example.chatting.utils.CreationDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Getter @Component
@NoArgsConstructor
//public class ChatMessage extends CreationDate {
public class ChatMessage extends CreationDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long roomId;

    private Long senderId;

    private String senderName;

    private String senderNickname;

    @Column(nullable = false)
    private String message;

    @Column
    private String type;

    @Column(nullable = false)
    private Boolean isRead;

    // 이미지 첨부시
    @Column
    private String img;


    public static ChatMessage createOf(ChatMessageRequestDto requestDto, String username, String nickname) {

        ChatMessage message = new ChatMessage();

        message.senderName = username;
        message.senderNickname = nickname;
        message.senderId = requestDto.getSenderId();
        message.roomId = requestDto.getRoomId();
        message.message = requestDto.getMessage();
        message.isRead = requestDto.getIsRead();
        message.type = requestDto.getType();

        return message;
    }

//    public static ChatMessage createInitOf(Long roomId) {
//
//        ChatMessage message = new ChatMessage();
//
//        message.roomId = roomId;
//        message.senderId = roomId;
////        message.message = "채팅방이 개설되었습니다.";
//        message.isRead = true;
//        message.type = "STATUS";
//
//        return message;
//    }

    public static ChatMessage createOutOf(Long roomId, User user) {

        ChatMessage message = new ChatMessage();

        message.roomId = roomId;
        message.senderNickname = user.getNickname();
        message.senderName = user.getUsername();
        message.message = user.getNickname()  + "님이 채팅방을 나갔습니다.";
        message.isRead = true;
//        message.type = "STATUS";

        return message;
    }



    // 이미지 사용시
    public void setImg(String img) {
        this.img = img;
    }
    public ChatMessage(Long roomId, Long userId, String message) {
        this.roomId = roomId;
        this.senderId = userId;
        this.message = message;
    }




}
