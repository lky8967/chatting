package com.example.chatting.config;


import com.example.chatting.jwt.HeaderTokenExtractor;
import com.example.chatting.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor { // 이론상 웹소켓이 실행되기 전에 작동한다고 합니다.
    private final JwtTokenProvider jwtTokenProvider;
    private final HeaderTokenExtractor headerTokenExtractor;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor
                .getAccessor(message, StompHeaderAccessor.class);

        assert accessor != null;

        switch (Objects.requireNonNull(accessor.getCommand())){
            case CONNECT:
            case SUBSCRIBE:
            case SEND:
                checkVaild(accessor); break; // 유효성 검증
            default: break;
        }

        return message;
    }

    private void checkVaild(StompHeaderAccessor accessor){

        try {
            String token = headerTokenExtractor.extract(accessor.getFirstNativeHeader("Authorization"));
            String user = jwtTokenProvider.getUserPk(token);
        } catch (Exception e) {
            throw new CustomException(INVAILD_CONTENTS_TOKEN);
        }

    }
}