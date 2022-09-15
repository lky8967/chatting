package com.example.chatting.chat;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponseDto {


    private Long messageId;
    private Long senderId;
    private String senderName;
    private String senderNickname;
    private String message;
    private LocalDateTime date;
    private Boolean isRead = false;
    private String type;

    private String img;

    public ChatMessageResponseDto(ChatMessageRequestDto messageRequestDto) {
        this.messageId = Long.valueOf(messageRequestDto.getMessage());
        this.senderId = messageRequestDto.getSenderId();
        this.senderNickname = messageRequestDto.getNickname();
        this.message = messageRequestDto.getMessage();
        this.isRead = messageRequestDto.getIsRead();
        this.type = messageRequestDto.getType();

    }

    public static ChatMessageResponseDto createOf(ChatMessage message, String username, String nickname){

        ChatMessageResponseDto responseDto = new ChatMessageResponseDto();

        responseDto.senderName = username;
        responseDto.messageId = message.getId();
        responseDto.message = message.getMessage();
        responseDto.date = message.getCreatedAt();
        responseDto.type = message.getType();
        responseDto.senderNickname = nickname;

        return responseDto;
    }

    public static ChatMessageResponseDto createFrom(ChatMessage message){

        ChatMessageResponseDto responseDto = new ChatMessageResponseDto();

        responseDto.senderId = message.getSenderId();
        responseDto.messageId = message.getId();
        responseDto.message = message.getMessage();
        responseDto.date = message.getCreatedAt();
//        responseDto.type = message.getType();
        responseDto.type = "STATUS";
        responseDto.isRead = true;
        responseDto.senderName = message.getSenderName();
        responseDto.senderNickname = message.getSenderNickname();

        return responseDto;

    }

}
