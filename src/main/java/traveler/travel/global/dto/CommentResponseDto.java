package traveler.travel.global.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import traveler.travel.domain.account.entity.User;
import traveler.travel.domain.post.entity.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentResponseDto {

    private Long writerId;
    private String writerName;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    private boolean isDeleted;

    private List<CommentResponseDto> children = new ArrayList<>();
    public CommentResponseDto(Comment comment) {
        this.writerId = comment.getWriter().getId();
        this.writerName = comment.getWriter().getNickname();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.isDeleted = comment.isDeleted();

        for (Comment child : comment.getChildren()) {
            this.children.add(new CommentResponseDto(child));
        }
    }

}
