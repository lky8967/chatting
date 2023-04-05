package com.example.chatting.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.function.Function;


@Getter @AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String username;
    private String nickname;
    private String userImgUrl;
    private String introduction;
    private Boolean isBanned ;
    private String registerDate;


    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.userImgUrl = user.getUserImgUrl();
        this.introduction = user.getIntroduction();
        this.registerDate = user.getRegisterDate();
    }


    public static UserResponseDto bannedList(User user, boolean isBanned) {
        
        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.id = user.getId();
        userResponseDto.username = user.getUsername();
        userResponseDto.nickname = user.getNickname();
        userResponseDto.userImgUrl = user.getUserImgUrl();
        userResponseDto.introduction = user.getIntroduction();
        userResponseDto.isBanned = isBanned;
        
        return userResponseDto;
    }

}
