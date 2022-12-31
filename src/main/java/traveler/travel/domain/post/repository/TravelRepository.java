package traveler.travel.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import traveler.travel.domain.post.entity.Travel;

public interface TravelRepository extends JpaRepository<Travel, Long> {
}
