package com.example.chatting.chatroom;

import com.example.chatting.chat.ChatMessage;
import com.example.chatting.chat.ChatMessageRepository;
import com.example.chatting.chat.ChatMessageResponseDto;
import com.example.chatting.chatroom.banned.BannedRepository;
import com.example.chatting.chatroom.banned.BannedUser;
import com.example.chatting.chatroom.banned.BannedUserDto;
import com.example.chatting.exception.CustomException;
import com.example.chatting.security.UserDetailsImpl;
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
    private final SimpMessageSendingOperations messagingTemplate;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final BannedRepository bannedRepository;

    // 채팅방 만들기
    @Transactional
    public Long createRoom(Long requesterId, Long acceptorId){

        // 유효성 검사
        if (requesterId.equals(acceptorId) ) {
            throw new CustomException(CANNOT_CHAT_WITH_ME);
        }
        // 채팅 상대 찾아오기
        User acceptor = userRepository.findById(acceptorId)
                .orElseThrow( () -> new CustomException(NOT_FOUND_USER));
        User requester = userRepository.findById(requesterId)
                .orElseThrow( () -> new CustomException(NOT_FOUND_USER));
        // 채팅방 차단 회원인지 검색
        if (bannedRepository.existsByBannedUserAndUser(acceptor, requester)) {
            throw new CustomException(CHAT_USER_BANNED);
        }

        // 채팅방을 찾아보고, 없을 시 DB에 채팅방 저장
        ChatRoom chatRoom = roomRepository.findByUser(requester, acceptor)
                .orElseGet( () -> {
                    ChatRoom c = roomRepository.save(ChatRoom.createOf(requester, acceptor));
//               // 채팅방 개설 메시지 생성
//                    notificationRepository.save(Notification.createOf(c, acceptor)); // 알림 작성 및 전달
//                    messagingTemplate.convertAndSend("/sub/notification/" + acceptorId,
//                            chatMessageResponseDto.createFrom(
//                                    messageRepository.save(ChatMessage.createInitOf(c.getId()))
//                            )
//                   );
                    return c;
                });
        chatRoom.enter(); // 채팅방에 들어간 상태로 변경 -> 람다를 사용해 일괄처리할 방법이 있는지 연구해 보도록 합니다.

        return chatRoom.getId();
    }


    // 방을 나간 상태로 변경하기
    @Transactional
    public void exitRoom(Long roomId, Long userId) {
        // 회원 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER)
                );
        // 채팅방 찾아오기
        ChatRoom chatRoom = roomRepository.findByIdFetch(roomId).orElseThrow(
                () -> new NullPointerException("해당 채팅방이 존재하지 않습니다.")
        );

        if (chatRoom.getRequester().getId().equals(userId)) {
            chatRoom.reqOut(true);

            System.out.println("req가 나감 = " );
//            chatMessageRepository.TypeChatReqMessage(roomId, userId);
            chatMessageRepository.TypeChatReqMessage(roomId);

        } else if (chatRoom.getAcceptor().getId().equals(userId)) {
            chatRoom.accOut(true);

            System.out.println("acc가 나감 = " );
//            chatMessageRepository.TypeChatAccMessage(roomId, userId);
            chatMessageRepository.TypeChatAccMessage(roomId);

        } else {
            throw new CustomException(EXIT_INVAILED);
        }

        if (chatRoom.getAccOut() && chatRoom.getReqOut()) {
            roomRepository.deleteById(chatRoom.getId()); // 둘 다 나간 상태라면 방 삭제
        } else {
            // 채팅방 종료 메시지 전달 및 저장
//            messagingTemplate.convertAndSend("/sub/chat/room/" + chatRoom.getId(),
            messagingTemplate.convertAndSend("/api/chat/room/{userId}" + chatRoom.getId(),
                    ChatMessageResponseDto.createFrom(
                            messageRepository.save(ChatMessage.createOutOf(roomId, user))
                    )
            );
        }
    }

    // 사용자별 채팅방 전체 목록 가져오기
    public List<RoomResponseDto> getRooms(Long userId, String nickname) {
        // 회원 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        // 방 목록 찾기
        List<RoomDto> dtos = roomRepository.findAllWith(user);
        // 메시지 리스트 만들기
        return findMessages(dtos ,userId);
    }

    public List<RoomResponseDto> findMessages(List<RoomDto> roomDtos, Long userId ) {


        List<RoomResponseDto> list = new ArrayList<>();
//        List<RoomResponseDto> suffix = new ArrayList<>();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        for (RoomDto dto : roomDtos) {

            System.out.println("dto.getAccId() = " + dto.getAccId());
            System.out.println("dto.getReqId() = " + dto.getReqId());
            // 해당 방의 유저가 나가지 않았을 경우에는 배열에 포함해 줍니다.
            if ( dto.getAccId().equals(userId) ) {
                if (!dto.getAccOut()) { // 만약 Acc(내)가 나가지 않았다면
                    int unreadCnt = messageRepository.countMsg(dto.getReqId(), dto.getRoomId());
                    System.out.println("unreadCntunreadCntunreadCntunreadCntunreadCnt = " + unreadCnt);

                    Boolean isBanned = bannedRepository.existsByBannedUserIdAndUser(dto.getReqId(), user);
                    System.out.println("isBanned1 = " + isBanned);

//                    if (dto.getAccFixed()){
//                        prefix.add(RoomResponseDto.createOf( dto, unreadCnt, isBanned));
//                    } else {
//                        suffix.add(RoomResponseDto.createOf( dto, unreadCnt, isBanned));
//                    }
                    list.add(RoomResponseDto.createOf( dto, unreadCnt, isBanned));
                }
            } else if ( dto.getReqId().equals(userId) ){
                if (!dto.getReqOut()) { // 만약 Req(내)가 나가지 않았다면
                    int unreadCnt = messageRepository.countMsg(dto.getAccId(), dto.getRoomId());
                    System.out.println("unreadCntunreadCntunreadCntunreadCntunreadCnt22 = " + unreadCnt);

                    Boolean isBanned = bannedRepository.existsByBannedUserIdAndUser(dto.getAccId(), user);
                    System.out.println("isBanned2 = " + isBanned);

//                    if (dto.getReqFixed()){
//                        prefix.add(RoomResponseDto.createOf( dto, unreadCnt, isBanned));
//                    } else {
//                        suffix.add(RoomResponseDto.createOf( dto, unreadCnt, isBanned));
//                    }

                    list.add(RoomResponseDto.createOf( dto, unreadCnt, isBanned));
                }
            }
//            messageRepository.updateChatMessage(dto.getRoomId(),userId);
        }

//        prefix.addAll(suffix);
        return list;
    }

    // 회원 차단 기능
    public void setBanned(UserDetailsImpl userDetails, Long bannedId){

        User user = userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_REQUESTER)
                );
        User bannedUser = userRepository.findById(bannedId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER)
                );

        BannedUser banned = bannedRepository.findByUserAndBannedUser(user, bannedUser).orElse(null);

        if ( banned == null ) {
            bannedRepository.save(BannedUser.createOf(user, bannedUser));
        } else {
            throw new CustomException(ALREADY_BANNED);
        }
    }

    // 차단 회원 불러오기
    public List<BannedUserDto> getBanned(UserDetailsImpl userDetails){
        User user = userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

//        List<User> bannedUsers = bannedRepository.findAllMyBannedByUser(user);
        List<BannedUser> bannedUsers = bannedRepository.findAllMyBannedByUser(user);
        List<BannedUserDto> userDtos = new ArrayList<>();

        for (BannedUser banndUser : bannedUsers){
            userDtos.add(BannedUserDto.createFrom(banndUser));
        }

        return userDtos;
    }

    // 회원 차단 풀기
    @Transactional
    public void unblock(UserDetailsImpl userDetails, Long bannedId){

//        User user = userRepository.findById(userDetails.getUserId())
//                .orElseThrow(() -> new CustomException(NOT_FOUND_REQUESTER)
//                );
//        User bannedUser = userRepository.findById(bannedId)
//                .orElseThrow(() -> new CustomException(NOT_FOUND_USER)
//                );
//
            bannedRepository.deleteById(bannedId);

//        BannedUser banned = bannedRepository.findByUserAndBannedUser(user, bannedUser)
//                .orElseThrow(() -> new CustomException(NOT_FOUND_BANNED));
//
//        banned.unblock();
    }

}

