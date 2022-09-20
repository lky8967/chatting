package com.example.chatting.chatroom.sse;

import com.example.chatting.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class NotificationDto {

    private Long id;
    public static NotificationDto create(Notification notification) {
        return new NotificationDto(notification.getId());
    }

//    public static NotificationDto createOf(Notification notification, User user){
//
//        NotificationDto dto = new NotificationDto();
//
//        dto.id = notification.getId();
//
//        return dto;
//    }
}
