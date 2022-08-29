package com.example.chatting.user;

import com.example.chatting.exception.ApiResponseMessage;
import com.example.chatting.login.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/users/register")
    public ResponseEntity<ApiResponseMessage> userRegister(@RequestBody UserRequestDto userRequestDto) throws InterruptedException {
        userService.userRegister(userRequestDto);
        ApiResponseMessage message = new ApiResponseMessage("Success", "회원가입이 완료되었습니다", "", "");
        return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
    }

    // 아이디 중복체크
    @PostMapping("/api/users/register/idCheck")
    public ResponseEntity<ApiResponseMessage> idCheck(@RequestBody UserRequestDto userRequestDto){
        userService.useridCheck(userRequestDto);
        ApiResponseMessage message = new ApiResponseMessage("Success", "사용가능한 아이디입니다", "", "");
        return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
    }

    // 아이디 중복체크
    @PostMapping("/api/users/register/nickCheck")
    public ResponseEntity<ApiResponseMessage> nickCheck(@RequestBody UserRequestDto userRequestDto){
        userService.nickCheck(userRequestDto);
        ApiResponseMessage message = new ApiResponseMessage("Success", "사용가능한 닉네임입니다", "", "");
        return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
    }

    



}
