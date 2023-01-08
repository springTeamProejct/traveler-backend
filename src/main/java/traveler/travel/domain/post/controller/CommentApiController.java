package traveler.travel.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import traveler.travel.domain.account.Login;
import traveler.travel.domain.account.entity.User;
import traveler.travel.domain.post.entity.Comment;
import traveler.travel.domain.post.entity.Post;
import traveler.travel.domain.post.service.CommentService;
import traveler.travel.domain.post.service.PostService;
import traveler.travel.global.dto.CommentRequestDto;
import traveler.travel.global.dto.CommentResponseDto;
import traveler.travel.global.dto.ResponseDto;

import java.util.Map;


@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;
    private final PostService postService;

    // 댓글 작성
    @PostMapping("/")
    public ResponseDto<CommentResponseDto> addComment(@Login User user, @RequestBody CommentRequestDto commentDto) {
        Post post = postService.findOne(commentDto.getPostId());
        Comment comment = commentService.addComment(commentDto, user, post);

        return new ResponseDto<>(HttpStatus.CREATED.value(), new CommentResponseDto(comment, user));
    }

    // 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseDto<CommentResponseDto> modifyComment(@Login User user, @RequestBody Map<String, String> commentMap, @PathVariable Long commentId) {
        String content = commentMap.get("content");
        Comment comment = commentService.modifyComment(commentId, content, user);

        return new ResponseDto<>(HttpStatus.OK.value(), new CommentResponseDto(comment, user));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseDto<CommentResponseDto> deleteComment(@Login User user, @PathVariable Long commentId) {
        Comment comment = commentService.deleteComment(commentId, user);

        return new ResponseDto<>(HttpStatus.OK.value(), new CommentResponseDto(comment, user));
    }

}
