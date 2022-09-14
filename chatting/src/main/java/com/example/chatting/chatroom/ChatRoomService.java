package com.example.chatting.chatroom;



import com.example.chatting.chat.ChatMessage;
import com.example.chatting.chat.ChatMessageRepository;
import com.example.chatting.chat.ChatMessageResponseDto;
import com.example.chatting.exception.CustomException;
import com.example.chatting.user.User;
import com.example.chatting.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.example.chatting.exception.ErrorCode.*;


@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository roomRepository;
    private final ChatMessageRepository messageRepository;

    private final ChatMessage chatMessage;
    private final SimpMessageSendingOperations messagingTemplate;
    private final UserRepository userRepository;

    // 채팅방 만들기
    @Transactional
    public Long createRoom(Long requesterId, Long acceptorId){

        // 유효성 검사
        if (requesterId.equals(acceptorId) ) {
            throw new CustomException(CANNOT_CHAT_WITH_ME);
        }
        // 채팅 상대 찾아오기
        User acceptor = userRepository.findById(acceptorId)
                .orElseThrow( () -> new CustomException(NOT_FOUND_USER)
                );
        User requester = userRepository.findById(requesterId)
                .orElseThrow( () -> new CustomException(NOT_FOUND_USER)
                );
        // 채팅방을 찾아보고, 없을 시 DB에 채팅방 저장
        ChatRoom chatRoom = roomRepository.findByUser(requester, acceptor)
                .orElseGet( () -> {
                    ChatRoom c = roomRepository.save(ChatRoom.createOf(requester, acceptor));
//               // 채팅방 개설 메시지 생성
//                    notificationRepository.save(Notification.createOf(c, acceptor)); // 알림 작성 및 전달
//                    messagingTemplate.convertAndSend("/sub/notification/" + acceptorId,
//                            MessageResponseDto.createFrom(
//                                    messageRepository.save(ChatMessage.createInitOf(c.getId()))
//                            )
//                    );
                    return c;
                });
//        chatRoom.enter(); // 채팅방에 들어간 상태로 변경 -> 람다를 사용해 일괄처리할 방법이 있는지 연구해 보도록 합니다.

        return chatRoom.getId();
    }


    // 방을 나간 상태로 변경하기
    @Transactional
    public void exitRoom(Long id, Long userId) {
        // 회원 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER)
                );
        // 채팅방 찾아오기
        ChatRoom chatRoom = roomRepository.findByIdFetch(id).orElseThrow(
                () -> new NullPointerException("해당 아이디가 존재하지 않습니다.")
        );

        if (chatRoom.getRequester().getId().equals(userId)) {
            chatRoom.reqOut(true);
        } else if (chatRoom.getAcceptor().getId().equals(userId)) {
            chatRoom.accOut(true);
        } else {
            throw new CustomException(EXIT_INVAILED);
        }

        if (chatRoom.getAccOut() && chatRoom.getReqOut()) {
            roomRepository.deleteById(chatRoom.getId()); // 둘 다 나간 상태라면 방 삭제
        } else {
            // 채팅방 종료 메시지 전달 및 저장
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatRoom.getId(),
                    ChatMessageResponseDto.createFrom(
                            messageRepository.save(ChatMessage.createOutOf(id, user))
                    )
            );
        }
    }

    // 사용자별 채팅방 전체 목록 가져오기
    public List<RoomResponseDto> getRooms(Long userId, String nickname) {
        // 회원 찾기
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("해당 아이디가 존재하지 않습니다.")
        );
        // 방 목록 찾기
        List<RoomDto> dtos = roomRepository.findAllWith(user);
        // 메시지 리스트 만들기
        return getMessages(dtos, userId, nickname);
    }

    public List<RoomResponseDto> getMessages(List<RoomDto> roomDtos, Long userId , String nickname) {

        List<RoomResponseDto> prefix = new ArrayList<>();
        List<RoomResponseDto> suffix = new ArrayList<>();

        String type = chatMessage.getType();

        for (RoomDto dto : roomDtos) {
            // 해당 방의 유저가 나가지 않았을 경우에는 배열에 포함해 줍니다.
            if (dto.getAccId().equals(userId)) {
                if (!dto.getAccOut()) { // 만약 Acc(내)가 나가지 않았다면
                    int unreadCnt = messageRepository.countMsg(dto.getReqId(), dto.getRoomId());
//                    if (dto.getAccFixed()) {
//                        prefix.add(RoomResponseDto.createOf(type, UserTypeEnum.Type.ACCEPTOR, dto, unreadCnt, false));
//                    } else {
//                        suffix.add(RoomResponseDto.createOf(type, UserTypeEnum.Type.ACCEPTOR, dto, unreadCnt, false));
//                    }
                }
            } else if (dto.getReqId().equals(userId)) {
                if (!dto.getReqOut()) { // 만약 Req(내)가 나가지 않았다면
                    int unreadCnt = messageRepository.countMsg(dto.getAccId(), dto.getRoomId());
//                    if (dto.getReqFixed()) {
//                        prefix.add(RoomResponseDto.createOf(type, UserTypeEnum.Type.REQUESTER, dto, unreadCnt, false));
//                    } else {
//                        suffix.add(RoomResponseDto.createOf(type, UserTypeEnum.Type.REQUESTER, dto, unreadCnt, false));
//                    }
                }
            }
        }
        prefix.addAll(suffix);
        return prefix;
    }

    public enum UserTypeEnum {
        ACCEPTOR(Type.ACCEPTOR),
        REQUESTER(Type.REQUESTER);

        private final String userType;

        UserTypeEnum(String userType) {
            this.userType = userType;
        }

        public static class Type {
            public static final String ACCEPTOR = "ACCEPTOR";
            public static final String REQUESTER = "REQUESTER";
        }
    }
}

