package com.example.chatting.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Token {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private String key;
}
