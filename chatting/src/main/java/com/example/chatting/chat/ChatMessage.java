package com.example.chatting.chat;

import com.example.chatting.user.User;
import com.example.chatting.utils.CreationDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
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

    @Column(nullable = false)
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




}
