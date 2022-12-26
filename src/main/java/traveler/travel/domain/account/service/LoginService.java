package traveler.travel.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import traveler.travel.domain.account.entity.User;
import traveler.travel.domain.account.enums.Authority;
import traveler.travel.domain.account.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException("이메일이 없습니다."));

        return User.builder().email(user.getEmail())
                .password(user.getPassword())
                .authority(Authority.ROLE_USER)
                .build();
    }
}
