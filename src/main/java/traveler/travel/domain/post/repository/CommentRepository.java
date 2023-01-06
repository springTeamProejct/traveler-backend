package traveler.travel.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import traveler.travel.domain.post.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
