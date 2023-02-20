package traveler.travel.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import traveler.travel.domain.post.entity.File;

import java.util.List;
import java.util.Optional;

public interface UserImgRepository extends JpaRepository<File, Long> {

    List<File> findByOriginName(String originName);

    Optional<File> findById(Long id);
}
