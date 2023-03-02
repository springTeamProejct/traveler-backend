package traveler.travel.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import traveler.travel.domain.account.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNum(String phoneNum);

    public Optional<User> findByEmail(String email);

    List<User> findAllByOrderByIdAsc();

}


