package traveler.travel.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import traveler.travel.domain.account.entity.User;
import traveler.travel.domain.post.entity.Post;
import traveler.travel.domain.post.entity.Travel;
import traveler.travel.domain.post.enums.Category;
import traveler.travel.domain.post.repository.PostRepository;
import traveler.travel.domain.post.repository.TravelRepository;
import traveler.travel.global.dto.PostRequestDto;
import traveler.travel.global.exception.ForbiddenException;
import traveler.travel.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final TravelRepository travelRepository;

    // 게시글 작성
    @Transactional
    public Post write(PostRequestDto dto, User writer) {
        Post post = dto.toPost(writer);

        if (dto.getCategory().equals(Category.TRAVEL)) {
            Travel travel = Travel.builder()
                    .travelType(dto.getType())
                    .gender(dto.getGender())
                    .minAge(dto.getMinAge())
                    .maxAge(dto.getMaxAge())
                    .maxCnt(dto.getMaxCnt())
                    .location(dto.getLocation())
                    .dateTime(dto.getDateTime())
                    .build();
            post.setTravel(travelRepository.save(travel));
        }

        return postRepository.save(post);
    }

    // 게시글 찾기
    public Post findOne(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("P00"));
        if (post.getDeletedAt() != null) throw new NotFoundException("P01");
        return post;
    }

    // 게시글 열람
    @Transactional
    public Post view(Long postId) {
        Post post = findOne(postId);
        post.view();
        return post;
    }

    // 게시글 수정

    // 게시글 삭제
    @Transactional
    public Post delete(Long postId, User writer) {
        Post post = findOne(postId);

        // 작성자만 수정,삭제할 수 있음
        if (!post.getWriter().equals(writer)) {
            throw new ForbiddenException("P02");
        }

        post.delete(); // 삭제 처리
        return post;
    }

}
