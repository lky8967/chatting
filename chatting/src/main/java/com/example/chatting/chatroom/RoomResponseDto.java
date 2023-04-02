package com.example.chatting.chatroom;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RoomResponseDto {

    private Long roomId;
    private Long yourId;
    private Long myId;
    private String yourNickname;
    private String myNickname;
    private String message;
    private String acceptor;
    private String requester;
    private Long acceptorId;
    private Long requesterId;
    private LocalDateTime date;
    private Boolean isBanned;
    private int unreadCnt;
    private String requesterUserImgUrl;
    private String acceptorUserImgUrl;

    public static RoomResponseDto createOf(RoomDto dto, int unreadCnt, Boolean isBanned){

        RoomResponseDto responseDto = new RoomResponseDto();

        responseDto.isBanned = isBanned;
        responseDto.unreadCnt = unreadCnt;
        responseDto.roomId = dto.getRoomId();
        responseDto.message = dto.getMessage();
        responseDto.yourId = dto.getAccId();
        responseDto.yourNickname = dto.getAccNickname();
        responseDto.myId = dto.getReqId();
        responseDto.myNickname = dto.getReqNickname();
        responseDto.date = dto.getDate();
        responseDto.acceptor = dto.getAccNickname();
        responseDto.requester = dto.getReqNickname();
        responseDto.acceptorId = dto.getAccId();
        responseDto.requesterId = dto.getReqId();

        responseDto.requesterUserImgUrl = dto.getRequesterUserImgUrl();
        responseDto.acceptorUserImgUrl = dto.getAcceptorUserImgUrl();

        return responseDto;
    }


}
