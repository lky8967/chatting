package com.example.chatting.user;

import lombok.Getter;

@Getter
public class UserRequestDto {
    private String username;
    private String password;
    private String nickname;
    private String userImgUrl;
    private String introduction;
}
