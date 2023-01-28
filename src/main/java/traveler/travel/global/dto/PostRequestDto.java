package traveler.travel.global.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.travel.domain.account.entity.User;
import traveler.travel.domain.account.enums.Gender;
import traveler.travel.domain.post.entity.Post;
import traveler.travel.domain.post.entity.Travel;
import traveler.travel.domain.post.enums.Category;
import traveler.travel.domain.post.enums.TravelType;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    Category category;


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


    public Post toPost(User user) {
        return Post.builder()
                .writer(user)
                .title(title)
                .content(content)
                .category(category)
                .build();
    }

    public Travel toTravel() {
        return Travel.builder()
                .travelType(type)
                .gender(gender)
                .minAge(minAge)
                .maxAge(maxAge)
                .maxCnt(maxCnt)
                .location(location)
                .dateTime(dateTime)
                .build();
    }
}
