package com.example.chatting.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
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

    @Column
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

    public void updateUser(String nickname, String introduction, String userImgUrl) {
        this.nickname = nickname;
        this.introduction = introduction;
        this.userImgUrl = userImgUrl;
    }
}
