package com.example.chatting.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter @NoArgsConstructor
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String userImgUrl;

    @Column
    private String introduction;

    public User(String username, String password, String nickname, String userImgUrl, String introduction){

        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.userImgUrl = userImgUrl;
        this.introduction = introduction;

    }
}
