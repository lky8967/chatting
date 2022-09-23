package com.example.chatting.chat;

import com.example.chatting.chatroom.ChatRoom;
import com.example.chatting.chatroom.ChatRoomRepository;
import com.example.chatting.chatroom.sse.SseService;
import com.example.chatting.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final SseService sseService;

    // 메시지 DB 저장
    public ChatMessageResponseDto saveMessage(ChatMessageRequestDto requestDto, String username, String nickname) {

        ChatMessageRequestDto sendMessageDto = new ChatMessageRequestDto();

        System.out.println("requestDto.getRoomId() = " + requestDto.getRoomId());
        ChatRoom chatRoom = chatRoomRepository.findByIdFetch(requestDto.getRoomId()).orElseThrow(
                () -> new NullPointerException("해당 채팅방이 존재하지 않습니다.")
        );
        chatRoom.accOut(false);
        chatRoom.reqOut(false);


        User receiver = chatRoom.getAcceptor();
        User sender = chatRoom.getRequester();
        ChatMessage message = chatMessageRepository.save(ChatMessage.createOf(requestDto, username , nickname));
        // pub -> 채널 구독자에게 전달
//        redisMessagePublisher.publish(requestDto);
        // 알림 보내기
        sseService.send(receiver);
        sseService.sender(sender);

        return ChatMessageResponseDto.createOf(message, username , nickname);

    }


    // 채팅 메시지 발송하기
    public void sendMessage(ChatMessageRequestDto requestDto, String userId, ChatMessageResponseDto responseDto) {
        RoomMsgUpdateDto msgUpdateDto = RoomMsgUpdateDto.createFrom(requestDto);
//        ChatMessageRequestDto sendMessageDto = new ChatMessageRequestDto();
//        redisMessagePublisher.publish(requestDto);

        messagingTemplate.convertAndSend("/sub/api/chat/rooms/" + userId, msgUpdateDto); // 개별 채팅 목록 보기 업데이트
        messagingTemplate.convertAndSend("/sub/api/chat/room/" + requestDto.getRoomId(), responseDto); // 채팅방 내부로 메시지 전송
    }

    // 메시지 찾기
    public List<ChatMessageResponseDto> getMessage(Long roomId, Long userId , String nickname) {

        System.out.println("5555555555555555555555555555555555555 메시지찾기 getMessage nickname = " + nickname);
        // 메시지 찾아오기
        List<ChatMessage> messages = chatMessageRepository.findAllByRoomIdOrderByIdAsc(roomId);
        // responseDto 만들기

        List<ChatMessageResponseDto> responseDtos = new ArrayList<>();
        // 상대가 보낸 메시지라면 모두 읽음으로 처리 -> isRead 상태 모두 true로 업데이트
        chatMessageRepository.updateChatMessage(roomId, userId);
        for (ChatMessage message : messages) {
            responseDtos.add(ChatMessageResponseDto.createFrom(message));
        }
        return responseDtos;
    }
}
