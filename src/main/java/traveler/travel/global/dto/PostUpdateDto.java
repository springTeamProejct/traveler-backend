package traveler.travel.global.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.travel.domain.account.enums.Gender;
import traveler.travel.domain.post.enums.TravelType;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateDto {

    private String title;
    private String content;

    // 여행 동행 게시글일 경우만
    private TravelType type;
    private Gender gender;
    private Double xPos;
    private Double yPos;
    private String location;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;
    private Integer minAge;
    private Integer maxAge;
    private Integer maxCnt; // 모집 인원
    private Boolean gatherYn;

}