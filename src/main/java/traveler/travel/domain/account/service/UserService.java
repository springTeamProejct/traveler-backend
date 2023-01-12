package traveler.travel.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import traveler.travel.domain.account.entity.RefreshToken;
import traveler.travel.domain.account.repository.RefreshTokenRepository;
import traveler.travel.domain.post.entity.Post;
import traveler.travel.global.dto.*;
import traveler.travel.domain.account.entity.User;
import traveler.travel.domain.account.repository.UserRepository;
import traveler.travel.global.exception.BadRequestException;
import traveler.travel.global.exception.NotFoundException;
import traveler.travel.jwt.TokenProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    //일반 회원 가입
    @Transactional
    public void join(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = User.build(userDto);

        userRepository.save(user);
    }

    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean checkPhoneNumDuplicate(String phoneNum) {
        return userRepository.existsByPhoneNum(phoneNum);
    }


    @Transactional
    public TokenDto login(EmailLoginRequestDto dto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = dto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 UserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .userEmail(authentication.getName())
                .refreshToken(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByUserEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getRefreshToken().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }

    //가입된 유저의 전체 정보 출력
    //관리자만 기능 사용 가능
    @Transactional
    public List<UserDto> getAllUserList(UserDto adminInfo){
        //AdminInfo를 통해서 권한 체크
//        boolean authMatches = checkAuthority(adminInfo.getEmail());

//        if(authMatches == false){
//            throw new BadRequestException("J08");
//        }

        List<User> users = userRepository.findAllByOrderByIdAsc();
        List<UserDto> userDtoList = new ArrayList<>();

        for( User user : users){
            UserDto userDto = UserDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .phoneNum(user.getPhoneNum())
                    .birth(user.getBirth())
                    .nickname(user.getNickname())
                    .gender(String.valueOf(user.getGender()))
                    .build();

            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    //회원 수정
    //본인만 회원 수정이 가능할 수 있게 조건 추가 필요.
    @Transactional
    public void updateUser(Long id, UpdateUserDto userDto, User user){

        User userInfo = findOne(id);

        //로그인한 사람이 본인일 경우에만 메소드 사용 가능.
        //RefreshToken의 유무로 user의 로그인 확인 가능.

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userInfo.updateUser(userDto.getEmail(),
                userDto.getPassword(),
                userDto.getNickname(),
                userDto.getProfileImg(),
                userDto.getPhoneNum());
    }

    //단일 회원 정보 확인 기능
    //회원 당사자만 기능 사용 가능
    @Transactional
    public User getUser(Long id, User userDto){
        Optional<User> usersWrapper = Optional.ofNullable(findOne(id));

        //비밀번호를 입력해서 본인인증 절차
//        boolean matches = checkPassword(id, userDto.getPassword());
//
//        if(matches == false){
//            throw new BadRequestException("J06");
//        }

        User user = usersWrapper.get();

        return User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNum(user.getPhoneNum())
                .birth(user.getBirth())
                .nickname(user.getNickname())
                .gender(user.getGender())
                .authority(user.getAuthority())
                .build();
    }

    //user 탈퇴
    //비밀번호 확인을 통해서 본인 인증
    @Transactional
    public User deleteUser(Long id, GetUserAndDeleteDto userDto){

        //이메일을 통해서 db 조회가 필요하다.
        User user = findOne(id);

        //로그인한 사람이 자신일 경우에만 삭제 가능.
        //-> 비밀번호 확인을 통해 본인인증 확인.
        boolean matches = checkPassword(id, userDto.getPassword());
        if(matches == false){
            throw new BadRequestException("J06");
        }

        user.delete();
        return user;
    }

    //비밀번호 일치 확인
    public boolean checkPassword(Long id, String checkUserPassword){

        //db에 있지 않은 id 값
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("L00"));

        String realPassword = user.getPassword();
        boolean matches = passwordEncoder.matches(checkUserPassword, realPassword);
        return matches;
    }

    //회원의 권한 확인
    //시큐리티 컨피그에서 사용하게끔 수정 필요. -> 로그인을 하고 나서 권한을 admin만 가능하게요... 수정
    public boolean checkAuthority(String email){

        //email을 통해서 db에서 조회 후, 값이 없다면 exception
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new BadRequestException("L00"));

        //db에서 찾은 권한
        String adminAuth = String.valueOf(user.getAuthority());

        //만약 받은 권한과 db에서 찾은 권한이 같다면 true, 아니면 false
        if(adminAuth == "ROLE_ADMIN"){
            return true;
        }
        return false;
    }

    //user 아이디 찾기
    public User findOne(Long userId) {

        //아이디가 존재하지 않는 경우
        User user = userRepository.findById(userId).orElseThrow(() ->
                new BadRequestException("L00"));

        //이미 삭제된 아이디일 경우 true, 삭제가 안된 경우 false
        boolean matches = user.isDeleted();

        if(matches == true){
            throw new BadRequestException("J07");
        }

        return user;
    }
}
