package traveler.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import traveler.travel.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    public Optional<User> findByEmail(String email);
}