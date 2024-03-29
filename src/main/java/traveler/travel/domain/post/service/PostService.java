package traveler.travel.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import traveler.travel.domain.account.entity.User;
import traveler.travel.domain.account.enums.Gender;
import traveler.travel.domain.post.entity.Like;
import traveler.travel.domain.post.entity.LikeId;
import traveler.travel.domain.post.entity.Post;
import traveler.travel.domain.post.entity.Travel;
import traveler.travel.domain.post.enums.Category;
import traveler.travel.domain.post.repository.LikeRepository;
import traveler.travel.domain.post.repository.PostRepository;
import traveler.travel.domain.post.repository.TravelRepository;
import traveler.travel.global.dto.PostRequestDto;
import traveler.travel.global.dto.PostSearchCondition;
import traveler.travel.global.dto.PostUpdateDto;
import traveler.travel.global.exception.BadRequestException;
import traveler.travel.global.exception.ForbiddenException;
import traveler.travel.global.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final TravelRepository travelRepository;
    private final LikeRepository likeRepository;

    // 게시글 작성
    @Transactional
    public Post write(PostRequestDto dto, User writer) {
        Post post = dto.toPost(writer);

        if (dto.getCategory().equals(Category.TRAVEL)) {
            Travel travel = dto.toTravel();

            validateTravelCondition(travel, writer);
            post.setTravel(travelRepository.save(travel));
        }

        return postRepository.save(post);
    }

    // 게시글 찾기
    public Post findOne(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("P00"));
        if (post.isDeleted()) throw new NotFoundException("P01");
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
    @Transactional
    public Post update(Long postId, PostUpdateDto dto, User writer) {
        Post post = findOne(postId);

        // 작성자만 수정,삭제할 수 있음
        if (!isWriter(writer, post)) {
            throw new ForbiddenException("P02");
        }

        Travel travel = post.getTravel();

        if (post.getCategory().equals(Category.TRAVEL)) {

            // 자기 자신 외로 누가 더 참여해있으면 수정 불가
            if (travel.getNowCnt() > 1) {
                throw new ForbiddenException("P04");
            }

            travel.update(dto.getMaxCnt(), dto.getType(), dto.getGender()
                    , dto.getMaxAge(), dto.getMinAge()
                    , dto.getXPos(), dto.getYPos(), dto.getLocation(), dto.getDateTime());

            validateTravelCondition(travel, writer);
            travel.changeGatherYn(dto.getGatherYn());
        }

        post.update(dto.getTitle(), dto.getContent(), travel);

        return post;
    }


    // 게시글 삭제
    @Transactional
    public Post delete(Long postId, User writer) {
        Post post = findOne(postId);

        // 작성자만 수정,삭제할 수 있음
        if (!isWriter(writer, post)) {
            throw new ForbiddenException("P02");
        }

        post.delete(); // 삭제 처리
        return post;
    }

    // 게시글 좋아요
    @Transactional
    public Post updateLike(Long postId, User user) {
        Post post = findOne(postId);
        LikeId likeId = new LikeId(postId, user.getId());

        // 좋아요 안누른 게시글이면 좋아요 생성하고, 이미 누른 게시글이면 좋아요 취소
        Optional<Like> like = likeRepository.findById(likeId);
        if (like.isPresent()) {
            likeRepository.delete(like.get());
            post.cancelLike(like.get());
        } else {
            Like savedLike = likeRepository.save(new Like(likeId));
            post.addLike(savedLike);
        }

        return post;
    }

    // 게시글 검색
    public Page<Post> searchPost(PostSearchCondition condition, Pageable pageable) {
        return postRepository.search(condition, pageable);
    }

    // 유저가 여행 동행 조건을 만족하는지 체크
    public void validateTravelCondition(Travel travel, User user) {
        if (!travel.isGatherYn() // 모집중이 아니거나
                || travel.getNowCnt() >= travel.getMaxCnt() // 모집 인원이 이미 찼거나
                || travel.getDateTime().isBefore(LocalDateTime.now()) // 날짜가 지났거나
                || travel.getMinAge() > user.getAge() || travel.getMaxAge() < user.getAge() // 나이가 안맞거나
                || (!travel.getGender().equals(Gender.FREE) && !travel.getGender().equals(user.getGender())) // 성별이 안맞으면
        ) throw new BadRequestException("P03"); // 조건 만족X 에러 반환
    }

    public boolean isWriter(User user, Post post) {
        if (user == null) return false;
        return post.getWriter().equals(user);
    }
}
