package traveler.travel.domain.account.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import traveler.travel.domain.common.entity.BaseTimeEntity;
import traveler.travel.global.dto.UserDto;
import traveler.travel.domain.account.enums.AccountType;
import traveler.travel.domain.account.enums.Authority;
import traveler.travel.domain.account.enums.Gender;
import traveler.travel.domain.post.entity.File;

import javax.persistence.*;
import java.util.ArrayDeque;
import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String password;
    private String phoneNum;
    private String birth;
    private String nickname;

    @OneToOne(fetch = FetchType.LAZY)
    private File profileImg;

    @Enumerated(EnumType.STRING)
    private Authority authority;
    @Enumerated(EnumType.STRING)
    private AccountType accountType; // 이메일, 카카오, 네이버, 구글
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public void setAuthority(Authority authority){
        this.authority = authority;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public static User build(UserDto userDto){
        User user = User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .authority(Authority.ROLE_USER)
                .phoneNum(userDto.getPhoneNum())
                .birth(userDto.getBirth())
                .nickname(userDto.getNickname())
                .gender(Gender.valueOf(userDto.getGender()))
                .build();
        return user;
    }
}
