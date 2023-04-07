package com.example.chatting.login;

import com.example.chatting.jwt.JwtTokenProvider;
import com.example.chatting.jwt.Token;
import com.example.chatting.session.SessionManager;
import com.example.chatting.user.User;
import com.example.chatting.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @PostMapping("/api/users/login")
    public ResponseEntity<String> login(final HttpServletResponse response
                                        , final HttpSession session
                                        , @RequestBody LoginRequestDto loginRequestDto) {
        String login = loginService.login(loginRequestDto , session ,response);

        System.out.println(" login = " + login);
        return new ResponseEntity<>(login, HttpStatus.OK);
    }

    @PostMapping("/api/users/logout/{userId}")
    public ResponseEntity<String> logout(@PathVariable Long userId, final HttpServletRequest request , final HttpSession session) {

       loginService.logOut(userId , request, session);
        return new ResponseEntity<>("로그아웃", HttpStatus.OK);
    }

}
