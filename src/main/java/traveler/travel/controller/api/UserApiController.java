package traveler.travel.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import traveler.travel.controller.dto.ResponseDto;
import traveler.travel.dto.EmailAndPhoneAuthDto;
import traveler.travel.dto.UserDto;
import traveler.travel.service.MailService;
import traveler.travel.service.UserService;
import traveler.travel.util.RedisUtil;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserApiController {
    private final UserService userService;
    private final MailService mailService;
    private final RedisUtil redisUtil;

    //유저 회원가입
    @PostMapping()
    public ResponseDto<?> save(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> validatorResult = userService.validateHandling(bindingResult);

            return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), validatorResult);
        }

        System.out.println("save함수 호출");
        userService.join(userDto);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    //이메일 중복 확인(중복되면 true, 중복되지 않으면 false)
    @GetMapping("/signup/email")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email) {
        return ResponseEntity.ok(userService.checkEmailDuplicate(email));
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

