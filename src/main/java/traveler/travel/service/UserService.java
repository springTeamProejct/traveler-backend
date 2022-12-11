package traveler.travel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import traveler.travel.entity.User;
import traveler.travel.enums.Authority;
import traveler.travel.repository.UserRepository;

import javax.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional
    public void 회원가입(User user){
        String rawPassword = user.getPassword();
        String encPassword = encoder.encode(rawPassword);

        user.setPassword(encPassword);
        user.setAuthority(Authority.ROLE_USER);

        userRepository.save(user);
    }
}
