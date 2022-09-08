package com.example.chatting.chatroom;


import com.example.chatting.chat.ChatMessageResponseDto;
import com.example.chatting.chat.ChatMessageService;
import com.example.chatting.exception.OkDto;
import com.example.chatting.security.UserDetailsImpl;
import com.example.chatting.user.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
//@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService roomService;
    private final ChatMessageService messageService;


    // 채팅방 만들기
    @PostMapping("/api/chat/room")
    public Long createRoom(@AuthenticationPrincipal UserDetailsImpl userDetails,
                           @RequestBody UserRequestDto requestDto) {
        
        Long acceptorId = requestDto.getUserId();
        Long userid = userDetails.getUserId();
        System.out.println("------주는 쪽 userid = " + userid);
        System.out.println("------받는쪽  acceptorId = " + acceptorId);

        return roomService.createRoom(userid, acceptorId);
    }

    // 전체 채팅방 목록 가져오기
    @GetMapping("/api/chat/rooms")
    public List<RoomResponseDto> getRooms(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUserId();
        String nickname = userDetails.getNickname();
        System.out.println(" 전체 채팅방 목록 가져오기 userId = " + userId);
        System.out.println(" 전체 채팅방 목록 가져오기 nickname = " + nickname);

        return roomService.getRooms(userId , nickname);
    }

    // 개별 채팅방 메시지 불러오기
    @GetMapping("/api/chat/room/{roomId}")
    public List<ChatMessageResponseDto> getMessage(@PathVariable Long roomId ,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userid = userDetails.getUserId();
        String nickname = userDetails.getNickname();
        return messageService.getMessage(roomId, userid, nickname);
    }


    // 채팅방 나가기
    @GetMapping("/api/chat/room/exit/{roomId}")
    public ResponseEntity<OkDto> exitRoom(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @PathVariable Long roomId) {
        Long userid = userDetails.getUserId();
        roomService.exitRoom(roomId, userid);
        return ResponseEntity.ok().body(OkDto.valueOf("true"));
    }

    // 채팅 즐겨찾기 고정
//    @PutMapping("/api/room/{roomId}")
//    public ResponseEntity<OkDto> fixedRoom(@AuthenticationPrincipal UserDetailsImpl userDetails,
//                                           @PathVariable Long roomId) {
//
//        roomService.fixedRoom(roomId, userDetails);
//        return ResponseEntity.ok().body(OkDto.valueOf("true"));
//    }

    // 채팅 차단하기
//    @GetMapping("/api/room/banned/{userId}")
//    public ResponseEntity<OkDto> setBanned(@AuthenticationPrincipal UserDetailsImpl userDetails,
//                                           @PathVariable Long userId) {
//
//        roomService.setBanned(userDetails, userId);
//        return ResponseEntity.ok().body(OkDto.valueOf("true"));
//    }

    // 차단 유저 목록 보기
//    @GetMapping("/api/room/banned")
//    public List<BannedUserDto> getBanned(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return roomService.getBanned(userDetails);
//    }

    // 차단 유저 해제하기
//    @PutMapping("/api/room/banned/{userId}")
//    public ResponseEntity<OkDto> releaseBanned(@AuthenticationPrincipal UserDetailsImpl userDetails,
//                                               @PathVariable Long userId) {
//
//        roomService.releaseBanned(userDetails, userId);
//        return ResponseEntity.ok().body(OkDto.valueOf("true"));
//    }
}
