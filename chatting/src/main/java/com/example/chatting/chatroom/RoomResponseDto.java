package com.example.chatting.chatroom;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.example.chatting.chatroom.ChatRoomService.UserTypeEnum.Type.ACCEPTOR;
import static com.example.chatting.chatroom.ChatRoomService.UserTypeEnum.Type.REQUESTER;


@Getter
@NoArgsConstructor
public class RoomResponseDto {

    private Long roomId;
    private Long userId;
    private String senderNickname;
    private String senderName;
    private Long senderId;
    private String message;
    private LocalDateTime date;
    private Boolean isRead;
    private Boolean isBanned;
    private int unreadCnt;
    private String type;

    public static RoomResponseDto createOf(String type, String flag, RoomDto dto, int unreadCnt, Boolean isBanned){

        RoomResponseDto responseDto = new RoomResponseDto();

        responseDto.isBanned = isBanned;
        responseDto.unreadCnt = unreadCnt;
        responseDto.type = type;
        responseDto.roomId = dto.getRoomId();
        responseDto.message = dto.getMessage();
        responseDto.senderId = dto.getAccId();
        responseDto.senderNickname = dto.getAccNickname();
        responseDto.date = dto.getDate();
        responseDto.isRead = dto.getIsRead();

        switch ( flag ) {

            case ACCEPTOR:

                responseDto.userId = dto.getReqId();
                responseDto.senderNickname = dto.getReqNickname();
                break;

            case REQUESTER:

                responseDto.userId = dto.getAccId();
                responseDto.senderNickname = dto.getAccNickname();
                break;

            default: break;
        }

        return responseDto;
    }


}
