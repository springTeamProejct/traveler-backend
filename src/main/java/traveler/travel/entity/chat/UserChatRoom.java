package traveler.travel.entity.chat;

import lombok.Getter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@Getter
public class UserChatRoom {
    @EmbeddedId
    private UserChatRoomId id;
    private boolean hostYn;
}
