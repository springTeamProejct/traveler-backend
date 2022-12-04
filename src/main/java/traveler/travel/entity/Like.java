package traveler.travel.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "likes")
public class Like {

    @EmbeddedId
    private LikeId id;


}
