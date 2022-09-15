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
    private Long acceptorUserId;
    private Long requesterUserId;
    private String acceptorNickname;
    private String requesterUserNickname;
    private String message;
    private LocalDateTime date;
//    private Boolean isRead;
    private Boolean isBanned;
    private int unreadCnt;
//    private String type;

    public static RoomResponseDto createOf( String flag, RoomDto dto, int unreadCnt, Boolean isBanned){

        RoomResponseDto responseDto = new RoomResponseDto();

        responseDto.isBanned = isBanned;
        responseDto.unreadCnt = unreadCnt;
//        responseDto.type = type;
        responseDto.roomId = dto.getRoomId();
        responseDto.message = dto.getMessage();
        responseDto.acceptorUserId = dto.getAccId();
        responseDto.acceptorNickname = dto.getAccNickname();
        responseDto.date = dto.getDate();
//        responseDto.isRead = dto.getIsRead();

        switch ( flag ) {

            case ACCEPTOR:

                responseDto.requesterUserId = dto.getReqId();
                responseDto.requesterUserNickname = dto.getReqNickname();
                break;

            case REQUESTER:

                responseDto.acceptorUserId = dto.getAccId();
                responseDto.acceptorNickname = dto.getAccNickname();
                break;

            default: break;
        }

        return responseDto;
    }


}
