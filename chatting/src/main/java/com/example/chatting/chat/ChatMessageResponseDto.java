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
    private Boolean isRead ;
    private String AccType;
    private String ReqType;

    private String img;
    private Long acceptorId;
//    private String acceptorName;
//    private String acceptorNickname;


    public ChatMessageResponseDto(ChatMessageRequestDto messageRequestDto) {
//        this.messageId = Long.valueOf(messageRequestDto.getMessage());
        this.senderId = messageRequestDto.getSenderId();
        this.senderNickname = messageRequestDto.getNickname();
        this.message = messageRequestDto.getMessage();
        this.isRead = messageRequestDto.getIsRead();
        this.AccType = messageRequestDto.getAccType();
        this.ReqType = messageRequestDto.getReqType();

        this.acceptorId = messageRequestDto.getAcceptorId();

    }

    public static ChatMessageResponseDto createOf(ChatMessage message, String username, String nickname){

        ChatMessageResponseDto responseDto = new ChatMessageResponseDto();

        responseDto.senderName = username;
        responseDto.messageId = message.getId();
        responseDto.message = message.getMessage();
        responseDto.date = message.getCreatedAt();
        responseDto.AccType = message.getAccType();
        responseDto.ReqType = message.getReqType();
        responseDto.senderNickname = nickname;

        responseDto.acceptorId = message.getAcceptorId();

        return responseDto;
    }

    public static ChatMessageResponseDto createFrom(ChatMessage message){

        ChatMessageResponseDto responseDto = new ChatMessageResponseDto();

        responseDto.senderId = message.getSenderId();
        responseDto.messageId = message.getId();
        responseDto.message = message.getMessage();
        responseDto.date = message.getCreatedAt();
        responseDto.AccType = message.getAccType();
        responseDto.ReqType = message.getReqType();
        responseDto.isRead = message.getIsRead();
        responseDto.senderName = message.getSenderName();
        responseDto.senderNickname = message.getSenderNickname();

        responseDto.acceptorId = message.getAcceptorId();

        return responseDto;

    }

}
