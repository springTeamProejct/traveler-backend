package traveler.travel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import traveler.travel.dto.UserDto;
import traveler.travel.entity.User;
import traveler.travel.enums.Authority;
import traveler.travel.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    //일반 회원 가입
    @Transactional
    public void join(UserDto userDto) {
        User user = User.builder()
                .email(userDto.getEmail())
                .password(encoder.encode(userDto.getPassword()))
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);
    }

    //소셜 로그인 호출 함수
    @Transactional
    public void join(User user){
        userRepository.save(user);
    }

    //controller에서 발생한 에러를 전달 받아 다시 반환해주는 함수(반환된 값은 js에서 사용된다.)
    public Map<String, String> validateHandling(BindingResult bindingResult){
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error:bindingResult.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());

        }
        return validatorResult;
    }

    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean checkPhoneNumDuplicate(String phoneNum) {
        return userRepository.existsByPhoneNum(phoneNum);
    }
}
