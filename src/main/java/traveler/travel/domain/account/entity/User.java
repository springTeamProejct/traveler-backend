package traveler.travel.domain.account.entity;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import traveler.travel.domain.common.entity.BaseTimeEntity;
import traveler.travel.global.dto.UserDto;
import traveler.travel.domain.account.enums.AccountType;
import traveler.travel.domain.account.enums.Authority;
import traveler.travel.domain.account.enums.Gender;
import traveler.travel.domain.post.entity.File;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_file_id")
    private File profileImg;

    @Enumerated(EnumType.STRING)
    private Authority authority;
    @Enumerated(EnumType.STRING)
    private AccountType accountType; // 이메일, 카카오, 네이버, 구글
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public void setProfileImg(File profileImg){
        this.profileImg = profileImg;
    }

    public int getAge() {
        int curYear = LocalDateTime.now().getYear();
        return curYear - Integer.parseInt(birth.substring(0, 4)) + 1;
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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        final User user = (User) o;
        return Objects.equals(id, user.getId());
    }

    //update하는 메소드
    public void updateUser(String email,
                           String password,
                           String nickname,
                           String phoneNum){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phoneNum = phoneNum;
    }
}
