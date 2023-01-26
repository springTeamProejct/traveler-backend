package traveler.travel.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import traveler.travel.domain.post.entity.Post;
import traveler.travel.global.dto.PostSearchCondition;

public interface PostSearchRepository {
    Page<Post> search(PostSearchCondition condition, Pageable pageable);
}
