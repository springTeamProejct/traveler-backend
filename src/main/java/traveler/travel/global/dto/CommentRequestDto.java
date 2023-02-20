package traveler.travel.global.dto;

import lombok.*;
import traveler.travel.domain.post.entity.Comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    @NotNull
    private Long postId;
    private Long parentId;
    @NotBlank
    private String content;

}
