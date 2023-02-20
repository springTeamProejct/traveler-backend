package traveler.travel.domain.account.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import traveler.travel.domain.account.Login;
import traveler.travel.global.dto.*;
import traveler.travel.domain.account.entity.User;
import traveler.travel.global.exception.EmailDuplicateException;
import traveler.travel.global.exception.ErrorCode;
import traveler.travel.domain.account.repository.UserRepository;
import traveler.travel.domain.account.service.MailService;
import traveler.travel.domain.account.service.SmsService;
import traveler.travel.domain.account.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserApiController {
    private final UserService userService;
    private final MailService mailService;
    private final SmsService smsService;

    private final UserRepository userRepository;

    //유저 회원가입
    @PostMapping()
    public ResponseEntity save (UserDto user, MultipartFile file) throws IOException {
        Optional<User> alreadyUser = userRepository.findByEmail(user.getEmail());
        if(alreadyUser.isPresent()){
            throw new EmailDuplicateException("emailDuplicated", ErrorCode.EMAIL_DUPLICATION);
        }

        userService.join(user, file);
        //api 형식? 노션페이지

        return ResponseEntity.ok("success");
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
    public ResponseEntity sendAuthCode(@RequestBody EmailAndPhoneAuthDto authDto) throws Exception {

        if (authDto.getEmail() != null) {

            // 인증 메일 발송
            mailService.sendAuthMail(authDto.getEmail());
            return new ResponseEntity(HttpStatus.OK);

        } else if (authDto.getPhoneNum() != null) {

            // 인증 문자 발송
            smsService.sendAuthCode(authDto.getPhoneNum());
            return new ResponseEntity(HttpStatus.OK);

        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    // 인증
    @PostMapping("/signup/authcode/validate")
    public ResponseEntity<String> validateAuthCode(@RequestBody EmailAndPhoneAuthDto authDto) {

        String result = userService.checkAuthCode(authDto);

        if (result.equals("200")) {
            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

    }

    //회원 수정
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@Login User user,
                                            @PathVariable Long id,
                                          @RequestBody UpdateUserDto userDto){
        userService.updateUser(id, userDto, user);

        return ResponseEntity.ok(("Success"));
    }


    //단일 회원 정보 확인 기능
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@Login User user,
                           @PathVariable Long id){
        return ResponseEntity.ok(userService.getUser(id, user));
    }

    //전체 회원 리스트
    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> list(@Login User user){
        return ResponseEntity.ok(userService.getAllUserList(user));
    }

    //회원 삭제(회원 basetime entity가 메소드가 실행된 시간으로 Update)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id,
                                          @RequestBody GetUserAndDeleteDto userDto){
        userService.deleteUser(id, userDto);
        return ResponseEntity.ok("Success");
    }

    //본인이 작성한 댓글 보여주기
//    @GetMapping("/users/{userId}/comments")
//    public ResponseEntity<String> viewMyComments(@PathVariable Long userId){
//        userService.getAllUserCommentList(userId);
//        return null;
//    }

}

