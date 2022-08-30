package com.example.chatting.validator;


import com.example.chatting.user.User;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class UserValidator {

    /* 닉네임 중복 체크 */
    public void checkNickname(Optional<User> foundNickname) {
        if (foundNickname.isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임이 존재합니다.");
        }
    }

    /* 닉네임 유효성 검사 */
    public void checkNicknameIsValid(String nickname) {
        if (nickname.contains(" ")) {
            throw new IllegalArgumentException(
                    "닉네임은 공백을 포함할 수 없습니다.");
        }

        if (!Pattern.matches("^[a-zA-Z0-9가-힣]{2,8}$", nickname)) {
            throw new IllegalArgumentException(
                    "닉네임은 영문, 한글, 숫자로 이루어진 2~8자로 작성해주세요.");
        }
    }
}
