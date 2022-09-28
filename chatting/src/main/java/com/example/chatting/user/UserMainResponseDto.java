package com.example.chatting.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter @NoArgsConstructor
public class UserMainResponseDto {

    private List<UserResponseDto> userList;

    public UserMainResponseDto(List<UserResponseDto> result) {
        this.userList = result;
    }


}
