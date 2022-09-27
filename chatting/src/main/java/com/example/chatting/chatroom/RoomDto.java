package com.example.chatting.chatroom;


import java.time.LocalDateTime;

// Dto 명칭은 table과 같아야 인식하는 것으로 보입니다.
public interface RoomDto {

    Long getRoomId();
    Long getReqId();
    Long getAccId();
    String getAccNickname();
    String getReqNickname();
    Boolean getReqOut();
    Boolean getAccOut();
    Boolean getIsRead();
    Boolean getIsBanned();
    LocalDateTime getDate();
    String getMessage();

}
