package traveler.travel.domain.post.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PostFileId implements Serializable {
    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "post_id")
    private Long postId;
}
