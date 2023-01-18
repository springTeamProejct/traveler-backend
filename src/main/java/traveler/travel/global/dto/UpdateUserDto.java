package traveler.travel.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.travel.domain.post.entity.File;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {

//    닉네임, 프로필 사진, 비밀번호, 전화번호(인증 재필요), 이메일(인증 재필요)

    String email;
    String password;
    String nickname;
    String phoneNum;
    File profileImg;

    public void setPassword(String password){
        this.password = password;
    }
}
