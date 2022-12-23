package traveler.travel.domain.post.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class PostFile {

    @EmbeddedId
    private PostFileId id;

}
