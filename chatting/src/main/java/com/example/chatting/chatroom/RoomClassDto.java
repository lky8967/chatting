package com.example.chatting.chatroom;


import lombok.Getter;

import java.time.LocalDateTime;

// Dto 명칭은 table과 같아야 인식하는 것으로 보입니다.
@Getter
public class RoomClassDto {

    private Long RoomId;
    private Long reqId;
    private Long accId;
    private String accNickname;
    private String reqNickname;
    private Boolean reqOut;
    private Boolean accOut;
    private Boolean isRead;
    private Long isBanned;
    private LocalDateTime date;
    private String message;

}
