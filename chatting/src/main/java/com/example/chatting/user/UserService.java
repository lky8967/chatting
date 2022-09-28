package com.example.chatting.user;

import com.example.chatting.chatroom.banned.BannedRepository;
import com.example.chatting.exception.CustomException;
import com.example.chatting.s3service.S3Service;
import com.example.chatting.security.UserDetailsImpl;
import com.example.chatting.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.chatting.exception.ErrorCode.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    private final UserValidator userValidator;
    private final BannedRepository bannedRepository;

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

    //마이 페이지
    public UserResponseDto myPage(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("해당 아이디가 존재하지 않습니다.")
        );
        return new UserResponseDto(user);
    }

    // 유저 정보 보기
    public List<UserResponseDto> userInfo(Long userId, Long myId) {
        User user = userRepository.findById(myId).orElseThrow(
                () -> new NullPointerException("로그인 해주세요")
        );

        User yourUser = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("해당 아이디가 존재하지 않습니다.")
        );
        boolean bannedUser = bannedRepository.existsByUserAndBannedUser(user ,yourUser);

        System.out.println("bannedUser = " + bannedUser);

        List<UserResponseDto> bannedList = new ArrayList<>();

        bannedList.add(UserResponseDto.bannedList(user , bannedUser));

        return bannedList;
    }

    @Transactional
    public void updateUser(MultipartFile multipartFile, ProfileUpdateRequestDto updateDto, UserDetailsImpl userDetails){

        User user = userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));


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
//            if(!imgUser.equals("") && multipartFile.isEmpty()){
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
                System.out.println("이미지를 처음 올릴경우");
                userImgUrl = imgUrl.get("url");
                UserImg profileImg = new UserImg(userImgUrl);
                user.setUserImgUrl(profileImg.getUserImgUrl());
            }
            user.updateUser(updateDto.getNickname(), updateDto.getIntroduction(), userImgUrl);
        }
    }

    // 메인페이지 유저 조회 리스트로 묶는 버전
//    public List<UserMainResponseDto> userRandom(Long myId) {
//        List<User> users = userRepository.findAllById(myId);
////       Long userId = userRepository.findAllById(myId);
////        List<Long> yourIdList = users.stream().map(User::getId).collect(Collectors.toList());
//        User user = userRepository.findById(myId).orElseThrow(
//                () -> new NullPointerException("로그인 해주세요")
//        );
//        List<UserMainResponseDto> result = new ArrayList<>();
//
//        List<Boolean> bannedList = new ArrayList<>();
//        List<User> yourList = new ArrayList<>();
//
//        boolean bannedUser = false;
//        User yourUser = new User();
//
//
//        for (User yoursId : users) {
//            Long yourId = yoursId.getId();
//
//            yourUser = userRepository.findById(yourId).orElseThrow(
//                    () -> new NullPointerException("해당 아이디가 존재하지 않습니다.")
//            );
//
//            bannedUser = bannedRepository.existsByUserAndBannedUser(user, yourUser);
//
//            bannedList.add(bannedUser);
////            users.add(yourUser);
//        }
//        for(int i = 0 ; i < bannedList.size(); i++){
//            System.out.println(bannedList.get(i));
//            System.out.println(users.get(i).getId());
//            UserMainResponseDto userMainResponseDto = new UserMainResponseDto(bannedList.get(i) , users.get(i).getId());
//            result.add(userMainResponseDto);
//        }
//        return result;
//    }

    public UserMainResponseDto userRandom(Long myId) {
        List<User> users = userRepository.findAllById(myId);
        List<UserResponseDto> result = users.stream()
                .map(UserResponseDto::new)
                .filter(x -> !x.getId().equals(myId))
                .collect(Collectors.toList());
        return new UserMainResponseDto(result);
    }

    @Transactional
    public void deleteImg(UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        user.setUserImgUrl("");
    }
}
