package traveler.travel.dto;

import lombok.Getter;

@Getter
public class EmailAndPhoneAuthDto {
    private String phoneNum;
    private String email;
    private Type type;

    public enum Type{
        PHONE, EMAIL
    }
}
