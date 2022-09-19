package com.example.chatting.chatroom.banned;

import com.example.chatting.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor
public class BannedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banned_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User bannedUser;

    @Column(nullable = false)
    private Boolean isBanned;

    public void unblock(){}

    public static BannedUser createOf(User user, User bannedUser) {

        BannedUser banned = new BannedUser();

        banned.user = user;
        banned.bannedUser = bannedUser;
        banned.isBanned = true;

        return banned;

    }

}
