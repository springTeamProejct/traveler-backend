package traveler.travel.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import traveler.travel.domain.account.entity.User;
import traveler.travel.domain.account.enums.Gender;
import traveler.travel.domain.post.entity.Comment;
import traveler.travel.domain.post.entity.Post;
import traveler.travel.domain.post.entity.Travel;
import traveler.travel.domain.post.enums.Category;
import traveler.travel.domain.post.enums.TravelType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostDetailDto {
    private Long postId;
    private String title;
    private String content;
    private String category;
    private int views;
    private int likes;
    private Long writerId;
    private String writerName;
    private TravelDto travel;

    private LocalDateTime createdAt;

    private List<CommentResponseDto> comments = new ArrayList<>();

    public PostDetailDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory().name();
        this.views = post.getViewCnt();
        this.writerId = post.getWriter().getId();
        this.writerName = post.getWriter().getNickname();
        this.createdAt = post.getCreatedAt();
        likes = post.getLikes().size();

        if (post.getCategory().equals(Category.TRAVEL)) {
            this.travel = new TravelDto(post.getTravel());
        }

        for (Comment comment : post.getComments()) {
            if (comment.getParent() == null) {
                this.comments.add(new CommentResponseDto(comment));
            }
        }

    }

    @AllArgsConstructor
    @Getter
    static class TravelDto {
        private Long travelId;
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

//        private ArrayList<String> participants = new ArrayList<>(); // 참여자 명단
//        private boolean isParticipateIn; // 열람하고 있는 본인이 참여했는지 여부


        public TravelDto(Travel travel) {
            travelId = travel.getId();
            travelType = travel.getTravelType();
            travelGender = travel.getGender();
            xPos = travel.getXPos();
            yPos = travel.getYPos();
            location = travel.getLocation();
            dateTime = travel.getDateTime();
            minAge = travel.getMinAge();
            maxAge = travel.getMaxAge();
            gatherYn = travel.isGatherYn();
            maxCnt = travel.getMaxCnt();
            nowCnt = travel.getNowCnt();
        }
    }


}
