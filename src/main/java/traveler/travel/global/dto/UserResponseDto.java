package traveler.travel.global.dto;

import lombok.Getter;
import traveler.travel.domain.account.entity.User;
import traveler.travel.domain.account.enums.Gender;

@Getter
public class UserResponseDto {
    private String email;
    private String phoneNum;
    private String birth;
    private String nickname;
    private Gender gender;
    private Long profileImgId;

    public UserResponseDto(User user, boolean open) {
        this.nickname = user.getNickname();
        this.gender = user.getGender();
        this.profileImgId = user.getProfileImg().getId();

        if (open) {
            this.email = user.getEmail();
            this.phoneNum = user.getPhoneNum();
            this.birth = user.getBirth();
        }
    }
}
