package com.example.chatting.chatroom.banned;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BannedUserDto {

    private Long bannedId;
    private String nickname;
    private String profile;

    public static BannedUserDto createFrom(BannedUser banndedUser) {

        BannedUserDto dto = new BannedUserDto();
        dto.bannedId = banndedUser.getId();
        dto.nickname = banndedUser.getBannedUser().getNickname();
        dto.profile = banndedUser.getBannedUser().getUserImgUrl();

        return dto;
    }
}
