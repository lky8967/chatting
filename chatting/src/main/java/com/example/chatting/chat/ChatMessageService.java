package com.example.chatting.chat;

import org.springframework.stereotype.Service;

@Service
public class ChatMessageService {
//    public ChatMessageResponseDto saveMessage(ChatMessageRequestDto requestDto, String username, String nickname) {
//    }

    // 채팅 메시지 발송하기
//    public void sendMessage(ChatMessageRequestDto requestDto, String userId, ChatMessageResponseDto responseDto) {
//        RoomMsgUpdateDto msgUpdateDto = RoomMsgUpdateDto.createFrom(requestDto);
//        ChatMessageRequestDto sendMessageDto = new ChatMessageRequestDto();
////        redisMessagePublisher.publish(requestDto);
//
//        messagingTemplate.convertAndSend("/sub/chat/rooms/" + userId, msgUpdateDto); // 개별 채팅 목록 보기 업데이트
//        messagingTemplate.convertAndSend("/sub/chat/room/" + requestDto.getRoomId(), responseDto); // 채팅방 내부로 메시지 전송
//    }
}
