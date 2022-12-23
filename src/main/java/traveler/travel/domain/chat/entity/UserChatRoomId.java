package traveler.travel.domain.chat.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserChatRoomId implements Serializable {
    @Column(name = "chatroom_id")
    private Long chatRoomId;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_id")
    private Long userId;
}
