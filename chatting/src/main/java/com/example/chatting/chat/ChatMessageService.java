package com.example.chatting.chat;

import com.example.chatting.chatroom.ChatRoom;
import com.example.chatting.chatroom.ChatRoomRepository;
import com.example.chatting.chatroom.RoomMsgUpdateDto;
import com.example.chatting.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;


    public ChatMessageResponseDto saveMessage(ChatMessageRequestDto requestDto, String username, String nickname) {


        ChatMessageRequestDto sendMessageDto = new ChatMessageRequestDto();


        System.out.println("requestDto.getRoomId() = " + requestDto.getRoomId());
        ChatRoom chatRoom = chatRoomRepository.findByIdFetch(requestDto.getRoomId()).orElseThrow(
                () -> new NullPointerException("해당 채팅방이 존재하지 않습니다.")
        );
        chatRoom.accOut(false);
        chatRoom.reqOut(false);

        System.out.println("chatRoomchatRoomchatRoomchatRoomchatRoomchatRoomchatRoomchatRoomchatRoom = " + chatRoom);

        User receiver = chatRoom.getAcceptor();
        User sender = chatRoom.getRequester();
        ChatMessage message = chatMessageRepository.save(ChatMessage.createOf(requestDto, username , nickname));
        // pub -> 채널 구독자에게 전달
//        redisMessagePublisher.publish(requestDto);
        // 알림 보내기
//        notificationService.send(receiver);
//        notificationService.sender(sender);

        return ChatMessageResponseDto.createOf(message, username , nickname);

    }

    // 채팅 메시지 발송하기
    public void sendMessage(ChatMessageRequestDto requestDto, String userId, ChatMessageResponseDto responseDto) {
        RoomMsgUpdateDto msgUpdateDto = RoomMsgUpdateDto.createFrom(requestDto);
        ChatMessageRequestDto sendMessageDto = new ChatMessageRequestDto();
//        redisMessagePublisher.publish(requestDto);

        messagingTemplate.convertAndSend("/sub/chat/rooms/" + userId, msgUpdateDto); // 개별 채팅 목록 보기 업데이트
        messagingTemplate.convertAndSend("/sub/chat/room/" + requestDto.getRoomId(), responseDto); // 채팅방 내부로 메시지 전송
    }
}
