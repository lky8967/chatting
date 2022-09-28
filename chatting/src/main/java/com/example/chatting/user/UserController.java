package com.example.chatting.user;

import com.example.chatting.exception.ApiResponseMessage;
import com.example.chatting.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원 가입
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

    // 유저 상세 페이지
    @GetMapping("/api/users/{userId}")
    public List<UserResponseDto> userInfo(@PathVariable Long userId ,@AuthenticationPrincipal UserDetailsImpl userDetails){
        Long myId = userDetails.getUserId();
        return userService.userInfo(userId, myId);
    }

    // 마이 페이지
    @GetMapping("/api/users/myPage")
    public UserResponseDto myPage(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Long userId = userDetails.getUserId();
        return userService.myPage(userId);
    }

    // 유저 정보 수정
    @PutMapping("/api/users/updated")
    public ResponseEntity<ApiResponseMessage> update( @RequestPart(value = "userImgUrl", required = false) MultipartFile multipartFile,
                                                      @RequestParam("nickname") String nickname,
                                                      @RequestParam("introduction") String introduction,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ProfileUpdateRequestDto updateRequestDto = new ProfileUpdateRequestDto(nickname, introduction);

        userService.updateUser(multipartFile, updateRequestDto, userDetails);
        ApiResponseMessage message = new ApiResponseMessage("Success", "정보수정이 완료 되었습니다", "", "");
        return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
    }


    // 메인페이지 유저 조회 리스트로 묶는 버전
    @GetMapping("/api/users/usersRandom")
    public UserMainResponseDto usersRandom(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Long userId = userDetails.getUserId();
        return userService.userRandom(userId);
    }

    // 유저 프로필 기본 이미지로 바꾸기
    @PutMapping("/api/users/imgDeleted")
    public ResponseEntity<ApiResponseMessage> deleteImg(@AuthenticationPrincipal UserDetailsImpl userDetails){
        userService.deleteImg(userDetails);
        ApiResponseMessage message = new ApiResponseMessage("Success", "정보수정이 완료 되었습니다", "", "");
        return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
    }

}
