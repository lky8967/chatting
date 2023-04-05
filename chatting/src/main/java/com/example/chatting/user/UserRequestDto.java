package com.example.chatting.user;

import lombok.Getter;

@Getter
public class UserRequestDto {
    private Long userId;
    private String username;
    private String password;
    private String passwordCheck;
    private String nickname;
    private String userImgUrl;
    private String introduction;
    private String registerDate;
}
