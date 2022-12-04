package traveler.travel.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import traveler.travel.AccountType;
import traveler.travel.Authority;
import traveler.travel.Gender;

import javax.persistence.*;

@Entity
@Getter
public class User extends BaseTimeEntity {

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
}
