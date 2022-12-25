package traveler.travel.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import traveler.travel.global.dto.UserDto;
import traveler.travel.domain.account.entity.User;
import traveler.travel.domain.account.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    //일반 회원 가입
    @Transactional
    public void join(UserDto userDto) {
        User user = User.build(userDto);

        userRepository.save(user);
    }

    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean checkPhoneNumDuplicate(String phoneNum) {
        return userRepository.existsByPhoneNum(phoneNum);
    }
}
