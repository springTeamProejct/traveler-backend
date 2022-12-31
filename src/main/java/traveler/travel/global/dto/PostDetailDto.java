package traveler.travel.global.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import traveler.travel.domain.account.enums.Gender;
import traveler.travel.domain.post.entity.Post;
import traveler.travel.domain.post.enums.Category;
import traveler.travel.domain.post.enums.TravelType;
import java.time.LocalDateTime;

@Getter
public class PostDetailDto {
    private Long postId;
    private String title;
    private String content;
    private String category;
    private int views;
    private Long writerId;
    private String writerName;
    private TravelDto travel;

    public PostDetailDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory().name();
        this.views = post.getViewCnt();
        this.writerId = post.getWriter().getId();
        this.writerName = post.getWriter().getNickname();

        if (post.getCategory().equals(Category.TRAVEL)) {
            this.travel = TravelDto.builder()
                    .travelType(post.getTravel().getTravelType())
                    .travelGender(post.getTravel().getGender())
                    .minAge(post.getTravel().getMinAge())
                    .maxAge(post.getTravel().getMaxAge())
                    .gatherYn(post.getTravel().isGatherYn())
                    .nowCnt(post.getTravel().getNowCnt())
                    .maxCnt(post.getTravel().getMaxCnt())
                    .location(post.getTravel().getLocation())
                    .dateTime(post.getTravel().getDateTime())
                    .build();
        }

    }

    @AllArgsConstructor
    @Builder
    @Getter
    static class TravelDto {
        private TravelType travelType;
        private Gender travelGender;
        private double xPos;
        private double yPos;
        private String location;
        private LocalDateTime dateTime;
        private int minAge;
        private int maxAge;
        private boolean gatherYn;
        private int maxCnt; // 모집 인원
        private int nowCnt; // 참여 인원
    }

}
