package com.example.chatting.security;

import com.example.chatting.user.User;
import com.example.chatting.user.UserRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@NoArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {

    private User user;
//    private Long userId;
//    private String nickname;


    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }


    public String getIntroduction(){
        return user.getIntroduction();
    }
    public String getNickname() {
        return user.getNickname();
    }

    public Long getUserId() {
        return user.getId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

//
//    // UserRequestDto로부터 UserDetailsImpl 생성
//    public static UserDetailsImpl fromUserRequestDto(UserRequestDto requestDto){
//
//        UserDetailsImpl userDetails = new UserDetailsImpl();
//
//        userDetails.userId = requestDto.get
//        userDetails.nickname = requestDto.getNickname();
//
//        return userDetails;
//    }
//
//    // User로부터 UserDetailsImpl 생성
//    public static UserDetailsImpl fromUser(User user){
//
//        UserDetailsImpl userDetails = new UserDetailsImpl();
//
//        userDetails.userId = user.getId();
//        userDetails.nickname = user.getNickname();
//
//        return userDetails;
//    }
//
//    // 비어있는 UserDetailsImpl 생성
//    public static UserDetailsImpl createEmpty(){
//
//        UserDetailsImpl userDetails = new UserDetailsImpl();
//
//        userDetails.userId = null;
//        userDetails.nickname = null;
//
//        return userDetails;
//    }

}