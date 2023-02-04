package traveler.travel.global.dto;

import lombok.Getter;

@Getter
public class EmailAndPhoneAuthDto {
    private String phoneNum;
    private String email;
    private String code;
}
