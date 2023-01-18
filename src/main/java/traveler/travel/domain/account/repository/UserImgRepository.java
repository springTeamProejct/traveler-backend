package traveler.travel.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import traveler.travel.domain.post.entity.File;

public interface UserImgRepository extends JpaRepository<File, Long> {
}
