package traveler.travel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import traveler.travel.dto.UserDto;
import traveler.travel.entity.User;
import traveler.travel.enums.Authority;
import traveler.travel.repository.UserRepository;

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
                .password(userDto.getPassword())
                .authority(Authority.ROLE_USER)
                .build();

        userRepository.save(user);
    }

    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
}
