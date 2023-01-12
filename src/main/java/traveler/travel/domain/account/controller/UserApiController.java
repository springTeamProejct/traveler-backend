package traveler.travel.domain.account.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import traveler.travel.domain.account.Login;
import traveler.travel.global.dto.*;
import traveler.travel.domain.account.entity.User;
import traveler.travel.global.exception.EmailDuplicateException;
import traveler.travel.global.exception.ErrorCode;
import traveler.travel.domain.account.repository.UserRepository;
import traveler.travel.domain.account.service.MailService;
import traveler.travel.domain.account.service.SmsService;
import traveler.travel.domain.account.service.UserService;
import traveler.travel.global.util.RedisUtil;

import java.util.List;
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


    //유저 회원가입
    @PostMapping()
    public ResponseDto<String> save (@RequestBody UserDto user){
        Optional<User> alreadyUser = userRepository.findByEmail(user.getEmail());
        if(alreadyUser.isPresent()){
            throw new EmailDuplicateException("emailDuplicated", ErrorCode.EMAIL_DUPLICATION);
        }

        userService.join(user);
        return new ResponseDto<String>(HttpStatus.OK.value(), "Success");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody EmailLoginRequestDto dto) {
        return ResponseEntity.ok(userService.login(dto));
    }

    //token 생성
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(userService.reissue(tokenRequestDto));
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

    //회원 수정
    @PutMapping("/{id}")
    public ResponseDto<String> updateUser(@Login User user,
                                            @PathVariable Long id,
                                          @RequestBody UpdateUserDto userDto){
        userService.updateUser(id, userDto, user);

        return new ResponseDto<String>(HttpStatus.OK.value(), "Success");
    }


    //단일 회원 정보 확인 기능
    @GetMapping("/{id}")
    public User getUser(@Login User user,
                           @PathVariable Long id){
        return userService.getUser(id, user);
    }

    //전체 회원 리스트
    @GetMapping("/all")
    public List<UserDto> list(@RequestBody UserDto userDto){
        return userService.getAllUserList(userDto);
    }

    //회원 삭제(회원 basetime entity가 메소드가 실행된 시간으로 Update)
    @DeleteMapping("/{id}")
    public ResponseDto<String> deleteUser(@PathVariable Long id,
                                          @RequestBody GetUserAndDeleteDto userDto){
        userService.deleteUser(id, userDto);
        return new ResponseDto<String>(HttpStatus.OK.value(), "Success");
    }
}

