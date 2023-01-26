package traveler.travel.global.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.travel.domain.account.enums.Gender;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSearchCondition {
    private KeywordType keywordType;
    private String keyword;

    private Boolean gatherYn;
    private Gender gender;
    private Integer minAge;
    private Integer maxAge;

    public enum KeywordType {
        TITLE, CONTENT, LOCATION, ALL
    }
}
