package traveler.travel.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import traveler.travel.domain.account.Login;
import traveler.travel.domain.account.entity.User;
import traveler.travel.domain.post.entity.Comment;
import traveler.travel.domain.post.entity.Post;
import traveler.travel.domain.post.service.CommentService;
import traveler.travel.domain.post.service.PostService;
import traveler.travel.global.dto.CommentRequestDto;
import traveler.travel.global.dto.CommentResponseDto;

import java.util.Map;


@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;
    private final PostService postService;

    // 댓글 작성
    @PostMapping("/")
    public ResponseEntity<CommentResponseDto> addComment(@Login User user, @RequestBody CommentRequestDto commentDto) {
        Post post = postService.findOne(commentDto.getPostId());
        Comment comment = commentService.addComment(commentDto, user, post);

        return new ResponseEntity<>(new CommentResponseDto(comment, user), HttpStatus.CREATED);
    }

    // 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> modifyComment(@Login User user, @RequestBody Map<String, String> commentMap, @PathVariable Long commentId) {
        String content = commentMap.get("content");
        Comment comment = commentService.modifyComment(commentId, content, user);

        return ResponseEntity.ok(new CommentResponseDto(comment, user));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> deleteComment(@Login User user, @PathVariable Long commentId) {
        Comment comment = commentService.deleteComment(commentId, user);

        return ResponseEntity.ok(new CommentResponseDto(comment, user));
    }

}
