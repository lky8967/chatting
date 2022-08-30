package com.example.chatting.user;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateRequestDto {

    private String userImgUrl;
    private String nickname;
    private String introduction;

    public ProfileUpdateRequestDto(String nickname, String introduction) {
        this.nickname = nickname;
        this.introduction = introduction;
    }

}
