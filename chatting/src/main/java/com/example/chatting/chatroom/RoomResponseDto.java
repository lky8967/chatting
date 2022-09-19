package com.example.chatting.chatroom;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//import static com.example.chatting.chatroom.ChatRoomService.UserTypeEnum.Type.ACCEPTOR;
//import static com.example.chatting.chatroom.ChatRoomService.UserTypeEnum.Type.REQUESTER;


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
//    private Boolean isRead;
    private Boolean isBanned;
    private int unreadCnt;
//    private String type;

//    public static RoomResponseDto createOf( String flag, RoomDto dto, int unreadCnt, Boolean isBanned){
    public static RoomResponseDto createOf( RoomDto dto, int unreadCnt, Boolean isBanned){

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

//        switch ( flag ) {
//
//            case "ACCEPTOR":
//
//                responseDto.myId = dto.getReqId();
//                responseDto.myNickname = dto.getReqNickname();
//                break;
//
//            case "REQUESTER":
//
//                responseDto.yourId = dto.getAccId();
//                responseDto.yourNickname = dto.getAccNickname();
//                break;
//
//            default: break;
//        }

        return responseDto;
    }


}
