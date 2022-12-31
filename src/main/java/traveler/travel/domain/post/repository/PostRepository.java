package traveler.travel.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import traveler.travel.domain.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
