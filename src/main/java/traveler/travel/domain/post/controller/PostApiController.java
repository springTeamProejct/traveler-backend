package traveler.travel.domain.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import traveler.travel.domain.post.entity.Post;
import traveler.travel.domain.post.service.PostService;
import traveler.travel.global.dto.PostDetailDto;
import traveler.travel.global.dto.PostRequestDto;
import traveler.travel.global.dto.ResponseDto;
import traveler.travel.jwt.UserAccount;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Slf4j
public class PostApiController {

    private final PostService postService;

    // 게시글 작성
    @PostMapping("/")
    public ResponseDto<PostDetailDto> createPost(@AuthenticationPrincipal UserAccount account
            , @RequestBody PostRequestDto postDto) {
        Post post = postService.write(postDto, account.getUser());
        return new ResponseDto<>(HttpStatus.CREATED.value(), new PostDetailDto(post));
    }

    // 게시글 단일 조회
    @GetMapping("/{postId}")
    public ResponseDto<PostDetailDto> findPost(@PathVariable("postId") Long postId) {
        Post post = postService.view(postId);
        return new ResponseDto<>(HttpStatus.OK.value(), new PostDetailDto(post));
    }

    // 게시글 수정

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseDto<PostDetailDto> deletePost(@AuthenticationPrincipal UserAccount account, @PathVariable("postId") Long postId) {
        Post post = postService.delete(postId, account.getUser());
        return new ResponseDto<>(HttpStatus.OK.value(), new PostDetailDto(post));
    }

}
