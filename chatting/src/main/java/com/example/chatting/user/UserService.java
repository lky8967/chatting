package com.example.chatting.user;

import com.example.chatting.s3service.S3Service;
import com.example.chatting.security.UserDetailsImpl;
import com.example.chatting.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    private final UserValidator userValidator;
//    private final BCryptPasswordEncoder encoder;

    // 회원 가입
    @Transactional
    public void userRegister(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        String password = passwordEncoder.encode(userRequestDto.getPassword());
        String passwordCheck = passwordEncoder.encode(userRequestDto.getPasswordCheck());
        String userImgUrl = userRequestDto.getUserImgUrl();
        String nickname = userRequestDto.getNickname();
        String introduction = userRequestDto.getIntroduction();

        // 회원 ID 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        User user = new User(username, password, passwordCheck , nickname , userImgUrl , introduction);
        userRepository.save(user);

    }

    //아이디 중복 체크
    public void useridCheck(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 아이디가 존재합니다.");
        }
    }

    //닉네임 중복 체크
    public void nickCheck(UserRequestDto userRequestDto) {
        String nickname = userRequestDto.getNickname();
        Optional<User> found = userRepository.findByNickname(nickname);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임이 존재합니다.");
        }
    }

    //유저상세 페이지 , 마이 페이지
    public UserResponseDto userInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("해당 아이디가 존재하지 않습니다.")
        );
        return new UserResponseDto(user);
    }


    @Transactional
    public void updateUser(MultipartFile multipartFile, ProfileUpdateRequestDto updateDto, UserDetailsImpl userDetails){

        User user = userRepository.findById(userDetails.getUserId()).orElseThrow(
                () -> new NullPointerException("해당 아이디가 존재하지 않습니다.")
        );

    // 닉네임 중복검사용
        Optional<User> foundNickname = userRepository
                .findByNickname(updateDto.getNickname());

        String nickname = user.getNickname();
        String introduction = user.getIntroduction();

        // 자기소개를 수정하지 않을경우
        if (updateDto.getIntroduction().equals("")){
            System.out.println("자기소개가 빈값일 경우");
            updateDto.getIntroduction(introduction);
        }

        if (updateDto.getNickname() != null) {
            // 변경하고자 하는 닉네임과 동일하면 유효성 검사하지 않음
            if (!updateDto.getNickname().equals(nickname)) {
                // 닉네임 중복 검사
                userValidator.checkNickname(foundNickname);
                // 닉네임 유효성 검사
//                userValidator.checkNicknameIsValid(updateDto.getNickname());
            }
//            nickname = updateDto.getNickname();

            String userImgUrl = "";
            String imgUser = user.getUserImgUrl();

            // 기본 이미지가 있을경우
            if(!imgUser.equals("") && multipartFile == null){
                System.out.println("기본 이미지일경우");
                userImgUrl = imgUser;
//                UserImg profileImg = new UserImg(userImgUrl);
//                user.setUserImgUrl(profileImg.getUserImgUrl());
                
            } else if (!imgUser.equals("") && multipartFile != null) {
                Map<String, String> imgUrl = s3Service.uploadFile(multipartFile);
                System.out.println("이미지 바꿀 경우");
                userImgUrl = imgUrl.get("url");
                UserImg profileImg = new UserImg(userImgUrl);
                user.setUserImgUrl(profileImg.getUserImgUrl());
            }

            // 프로필 이미지를 직접 업로드 했을 경우
            else if (multipartFile != null) {
                Map<String, String> imgUrl = s3Service.uploadFile(multipartFile);
                System.out.println("여긴 안옴?");
                userImgUrl = imgUrl.get("url");
                UserImg profileImg = new UserImg(userImgUrl);
                user.setUserImgUrl(profileImg.getUserImgUrl());
            }
            user.updateUser(updateDto.getNickname(), updateDto.getIntroduction(), userImgUrl);
        }

    }


    // 메인페이지 유저 조회 리스트로 묶는 버전
    public UserMainResponseDto userRandom() {
        List<User> users = userRepository.findAllById();
        List<UserResponseDto> result = users.stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
        return new UserMainResponseDto(result);
    }

    @Transactional
    public void deleteImg(UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUserId()).orElseThrow(
                () -> new NullPointerException("해당 아이디가 존재하지 않습니다.")
        );
        user.setUserImgUrl("");
    }
}
