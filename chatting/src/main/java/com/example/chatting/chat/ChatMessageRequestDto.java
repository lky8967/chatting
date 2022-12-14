package com.example.chatting.chat;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequestDto {
    // 메시지 타입 : 입장, 채팅
    private Long roomId;
    private Long senderId;
    private String nickname;
    private String message;
    private String AccType;
    private String ReqType;
    private Boolean isRead;
    private Long acceptorId;
}
