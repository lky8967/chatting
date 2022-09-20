package com.example.chatting.user;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String username;
    private String nickname;
    private String userImgUrl;
    private String introduction;
    private String isBanned;


    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.userImgUrl = user.getUserImgUrl();
        this.introduction = user.getIntroduction();
    }


}
