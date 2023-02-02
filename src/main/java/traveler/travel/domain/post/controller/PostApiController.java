package traveler.travel.domain.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import traveler.travel.domain.account.Login;
import traveler.travel.domain.account.entity.User;
import traveler.travel.domain.post.entity.Post;
import traveler.travel.domain.post.service.PostService;
import traveler.travel.global.dto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Slf4j
public class PostApiController {

    private final PostService postService;

    // 게시글 작성
    @PostMapping("/")
    public ResponseEntity<PostDetailDto> createPost(@Login User user
            , @RequestBody PostRequestDto postDto) {
        Post post = postService.write(postDto, user);
        return new ResponseEntity<>(new PostDetailDto(post, user), HttpStatus.CREATED);
    }

    // 게시글 단일 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailDto> findPost(@Login User user, @PathVariable("postId") Long postId) {
        Post post = postService.view(postId);
        return ResponseEntity.ok(new PostDetailDto(post, user));
    }

    // 게시글 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<PostDetailDto> modifyPost(@Login User user, @PathVariable("postId") Long postId
            , @RequestBody PostUpdateDto postDto) {
        Post post = postService.update(postId, postDto, user);
        return ResponseEntity.ok(new PostDetailDto(post, user));
    }


    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity deletePost(@Login User user, @PathVariable("postId") Long postId) {
        Post post = postService.delete(postId, user);
        return ResponseEntity.ok().build();
    }

    // 게시글 좋아요
    @PostMapping("/{postId}/like")
    public ResponseEntity<PostDetailDto> likeUpdate(@Login User user, @PathVariable("postId") Long postId) {
        Post post = postService.updateLike(postId, user);
        return ResponseEntity.ok(new PostDetailDto(post, user));
    }

    // 게시글 조회, 검색
    @GetMapping
    public ResponseEntity<Page<PostDetailDto>> searchPosts(PostSearchCondition condition, Pageable pageable) {
        Page<Post> posts = postService.searchPost(condition, pageable);
        return ResponseEntity.ok(posts.map(p -> new PostDetailDto(p, null)));
    }
}
