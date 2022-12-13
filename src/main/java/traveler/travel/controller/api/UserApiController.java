package traveler.travel.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import traveler.travel.controller.dto.ResponseDto;
import traveler.travel.entity.User;
import traveler.travel.exception.EmailDuplicateException;
import traveler.travel.exception.ErrorCode;
import traveler.travel.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import traveler.travel.dto.EmailAndPhoneAuthDto;
import traveler.travel.service.MailService;
import traveler.travel.service.UserService;
import traveler.travel.util.RedisUtil;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserApiController {

    private final UserService userService;
    private final MailService mailService;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    //유저 회원가입
    @PostMapping()
    public ResponseDto<?> save(@RequestBody User user){
        Optional<User> alreadyUser = userRepository.findByEmail(user.getEmail());
        if(alreadyUser.isPresent()){
            throw new EmailDuplicateException("emailDuplicated", ErrorCode.EMAIL_DUPLICATION);
        }

        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), "success");
    }

    public Optional<User> findUserByEmail(String email){
        Optional<User> alreadyUser = userRepository.findByEmail(email);
        return alreadyUser;
    }


    // 이메일, 전화번호 인증 발송
    @PostMapping("/signup/authcode")
    public ResponseEntity sendAuthCode(@RequestBody EmailAndPhoneAuthDto authDto) throws Exception {
        if (authDto.getType().equals(EmailAndPhoneAuthDto.Type.EMAIL)) {
            // 이메일 중복 확인
            if (userService.checkEmailDuplicate(authDto.getEmail())) {
                // 이미 가입된 이메일 주소
                return ResponseEntity.ok(false);
            } else {
                // 인증 메일 발송
                String code = mailService.sendAuthMail(authDto.getEmail());

                return new ResponseEntity(HttpStatus.OK);
            }
        } else if (authDto.getType().equals(EmailAndPhoneAuthDto.Type.PHONE)) {

            // 전화번호 중복 확인

            // 인증 문자 발송
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    // 인증
    @PostMapping("/signup/authcode/validate")
    public ResponseDto<String> validateAuthCode(@RequestBody EmailAndPhoneAuthDto authDto) {
        String value = authDto.getType().equals(EmailAndPhoneAuthDto.Type.EMAIL)? redisUtil.getValue(authDto.getEmail()) : redisUtil.getValue(authDto.getPhoneNum());
        if (value == null || !value.equals(authDto.getCode())) {
            // 유효하지 않은 인증번호
            return new ResponseDto<String>(HttpStatus.BAD_REQUEST.value(), "invalid code");
        } else {
            // 인증 성공
            return new ResponseDto<String>(HttpStatus.OK.value(), "success");
        }
    }
}

