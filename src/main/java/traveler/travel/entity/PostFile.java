package traveler.travel.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class PostFile {

    @EmbeddedId
    private PostFileId id;

}
