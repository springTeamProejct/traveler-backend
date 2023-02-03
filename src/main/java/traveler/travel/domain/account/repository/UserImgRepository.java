package traveler.travel.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import traveler.travel.domain.post.entity.File;

import java.util.Optional;

public interface UserImgRepository extends JpaRepository<File, Long> {

    Optional<File> findByOriginName(String originName);
}
