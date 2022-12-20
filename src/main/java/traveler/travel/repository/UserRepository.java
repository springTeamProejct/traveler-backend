package traveler.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import traveler.travel.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNum(String phoneNum);
}
