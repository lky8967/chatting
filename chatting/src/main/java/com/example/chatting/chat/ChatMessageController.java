package com.example.chatting.chat;

import com.example.chatting.jwt.HeaderTokenExtractor;
import com.example.chatting.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final JwtTokenProvider jwtTokenProvider;
    private final HeaderTokenExtractor extractor;


    // 해당 어노테이션을 통해 웹소켓으로 pub 되는 모든 메시지를 처리하게 됩니다. URI에 자동으로 접두어 /pub 이 붙습니다.
    @MessageMapping("/api/chat/message")
    public void message(ChatMessageRequestDto requestDto, @Header("Authorization") String token) throws IOException { // 토큰을 헤더에서 받는 것으로 최종 확정함

        String jwt = extractor.extract(token);
        String username = jwtTokenProvider.getUserPk(jwt);
        String nickname = jwtTokenProvider.getNickName(jwt);
        Long userId = requestDto.getSenderId();

        ChatMessageResponseDto responseDto = chatMessageService.saveMessage(requestDto, username ,  nickname); //DB에 저장
        chatMessageService.sendMessage(requestDto, username, responseDto);// 메시지를 sub 주소로 발송해줌
    }
}
