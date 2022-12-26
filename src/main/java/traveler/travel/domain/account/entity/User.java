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
public class User extends BaseTimeEntity implements UserDetails {

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

    @Column(length = 1000)
    private String refreshToken;

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void destroyRefreshToken(){
        this.refreshToken = null;
    }

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection < GrantedAuthority > collectors = new ArrayDeque<>();
        collectors.add(() -> {
            return "계정별 등록할 권한";
        });

        return collectors;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
