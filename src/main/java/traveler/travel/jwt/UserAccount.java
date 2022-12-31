package traveler.travel.jwt;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserAccount extends User {

    private final traveler.travel.domain.account.entity.User user;

    public UserAccount(traveler.travel.domain.account.entity.User user) {
        super(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority(user.getAuthority().toString())));
        this.user = user;
    }
}
