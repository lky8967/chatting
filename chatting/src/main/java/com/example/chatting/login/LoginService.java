package com.example.chatting.login;

import com.example.chatting.exception.CustomException;
import com.example.chatting.jwt.JwtTokenProvider;
import com.example.chatting.jwt.Token;
import com.example.chatting.session.CountManager;
import com.example.chatting.session.SessionManager;
import com.example.chatting.user.User;
import com.example.chatting.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

import static com.example.chatting.exception.ErrorCode.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final SessionManager sessionManager;


    public String login(LoginRequestDto loginRequestDto ,  HttpServletResponse response) {
        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        System.out.println("user = " + user);
//        if (user != null) {
        if (passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            String token = jwtTokenProvider.createToken(user.getUsername() ,user.getNickname(), user.getId() );
            sessionManager.createSession(user.getId() , response);

            System.out.println("접속자 수 : " + SessionManager.getCount());
            return token;
//            }
        } else {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다");
        }
//        throw new IllegalArgumentException("오류!");
    }

}
