package traveler.travel.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import traveler.travel.domain.post.entity.Like;
import traveler.travel.domain.post.entity.LikeId;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
}
