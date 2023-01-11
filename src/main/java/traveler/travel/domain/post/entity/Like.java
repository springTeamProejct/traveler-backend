package traveler.travel.domain.post.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

    @EmbeddedId
    private LikeId id;

    public Like(LikeId id) {
        this.id = id;
    }

}
