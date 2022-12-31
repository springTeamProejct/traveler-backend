package traveler.travel.domain.account.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class RefreshToken {
    @Id
    private String userEmail;
    private String refreshToken;

    @Builder
    public RefreshToken(String userEmail, String refreshToken) {
        this.userEmail = userEmail;
        this.refreshToken = refreshToken;
    }

    public RefreshToken updateValue(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }
}
