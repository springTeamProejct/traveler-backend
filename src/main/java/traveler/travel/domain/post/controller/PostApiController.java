package traveler.travel.domain.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import traveler.travel.domain.account.Login;
import traveler.travel.domain.account.entity.User;
import traveler.travel.domain.post.entity.Post;
import traveler.travel.domain.post.service.PostService;
import traveler.travel.global.dto.PostDetailDto;
import traveler.travel.global.dto.PostRequestDto;
import traveler.travel.global.dto.PostUpdateDto;
import traveler.travel.global.dto.ResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Slf4j
public class PostApiController {

    private final PostService postService;

    // 게시글 작성
    @PostMapping("/")
    public ResponseDto<PostDetailDto> createPost(@Login User user
            , @RequestBody PostRequestDto postDto) {
        Post post = postService.write(postDto, user);
        return new ResponseDto<>(HttpStatus.CREATED.value(), new PostDetailDto(post, true));
    }

    // 게시글 단일 조회
    @GetMapping("/{postId}")
    public ResponseDto<PostDetailDto> findPost(@Login User user, @PathVariable("postId") Long postId) {
        Post post = postService.view(postId);
        boolean isWriter = postService.isWriter(user, post);
        return new ResponseDto<>(HttpStatus.OK.value(), new PostDetailDto(post, isWriter));
    }

    // 게시글 수정
    @PatchMapping("/{postId}")
    public ResponseDto<PostDetailDto> modifyPost(@Login User user, @PathVariable("postId") Long postId
            , @RequestBody PostUpdateDto postDto) {
        Post post = postService.update(postId, postDto, user);
        boolean isWriter = postService.isWriter(user, post);
        return new ResponseDto<>(HttpStatus.CREATED.value(), new PostDetailDto(post, isWriter));
    }


    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseDto<PostDetailDto> deletePost(@Login User user, @PathVariable("postId") Long postId) {
        Post post = postService.delete(postId, user);
        return new ResponseDto<>(HttpStatus.OK.value(), new PostDetailDto(post, true));
    }

}
