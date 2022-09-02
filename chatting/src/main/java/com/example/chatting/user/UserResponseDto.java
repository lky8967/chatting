package com.example.chatting.user;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class UserResponseDto {
    private String username;
    private String nickname;
    private String userImgUrl;
    private String introduction;

    public UserResponseDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.userImgUrl = user.getUserImgUrl();
        this.introduction = user.getIntroduction();
    }


}
