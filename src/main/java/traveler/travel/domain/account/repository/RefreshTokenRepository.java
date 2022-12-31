package traveler.travel.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import traveler.travel.domain.account.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserEmail(String userEmail);
}
