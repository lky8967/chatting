package com.example.chatting.user;

import com.example.chatting.login.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
//    private final BCryptPasswordEncoder encoder;

    @Transactional
    public void userRegister(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        String password = passwordEncoder.encode(userRequestDto.getPassword());
        String userImgUrl = userRequestDto.getUserImgUrl();
        String nickname = userRequestDto.getNickname();
        String introduction = userRequestDto.getIntroduction();

        // 회원 ID 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        User user = new User(username, password, userImgUrl , introduction,   nickname);
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

    public void nickCheck(UserRequestDto userRequestDto) {
        String nickname = userRequestDto.getNickname();
        Optional<User> found = userRepository.findByNickname(nickname);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임이 존재합니다.");
        }
    }



}
