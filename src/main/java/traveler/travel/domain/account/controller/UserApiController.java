package traveler.travel.domain.account.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import traveler.travel.global.dto.ResponseDto;
import traveler.travel.global.dto.EmailAndPhoneAuthDto;
import traveler.travel.global.dto.UserDto;
import traveler.travel.domain.account.entity.User;
import traveler.travel.global.exception.EmailDuplicateException;
import traveler.travel.global.exception.ErrorCode;
import traveler.travel.domain.account.repository.UserRepository;
import traveler.travel.domain.account.service.MailService;
import traveler.travel.domain.account.service.SmsService;
import traveler.travel.domain.account.service.UserService;
import traveler.travel.global.util.RedisUtil;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserApiController {
    private final UserService userService;
    private final MailService mailService;
    private final RedisUtil redisUtil;
    private final SmsService smsService;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;


    //유저 회원가입
    @PostMapping()
    public ResponseDto<?> save (@RequestBody UserDto user){
        Optional<User> alreadyUser = userRepository.findByEmail(user.getEmail());
        if(alreadyUser.isPresent()){
            throw new EmailDuplicateException("emailDuplicated", ErrorCode.EMAIL_DUPLICATION);
        }

        user.setPassword(encoder.encode(user.getPassword()));
        userService.join(user);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    public Optional<User> findUserByEmail(String email){
        Optional<User> alreadyUser = userRepository.findByEmail(email);
        return alreadyUser;
    }


    // 이메일, 전화번호 인증 발송
    @PostMapping("/signup/authcode")
    public ResponseDto sendAuthCode(@RequestBody EmailAndPhoneAuthDto authDto) throws Exception {
        if (authDto.getType().equals(EmailAndPhoneAuthDto.Type.EMAIL)) {
            // 이메일 중복 확인
            if (userService.checkEmailDuplicate(authDto.getEmail())) {
                // 이미 가입된 이메일 주소
                return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), null);
            } else {
                // 인증 메일 발송
                String code = mailService.sendAuthMail(authDto.getEmail());
                return new ResponseDto(HttpStatus.OK.value(), null);
            }
        } else if (authDto.getType().equals(EmailAndPhoneAuthDto.Type.PHONE)) {
            // 전화번호 중복 확인
            if (userService.checkPhoneNumDuplicate(authDto.getPhoneNum())) {
                return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), null);
            } else {
                // 인증 문자 발송
                SmsService.SmsResponseDto response = smsService.sendAuthCode(authDto.getPhoneNum());
                return new ResponseDto(HttpStatus.OK.value(), response);
            }

        }
        return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), null);
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

