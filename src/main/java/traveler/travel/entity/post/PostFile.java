package traveler.travel.entity.post;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class PostFile {

    @EmbeddedId
    private PostFileId id;

}
