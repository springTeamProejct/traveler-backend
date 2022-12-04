package traveler.travel.entity.chat;

import traveler.travel.enums.ChatRoomType;
import traveler.travel.entity.BaseTimeEntity;

import javax.persistence.*;

@Entity
public class ChatRoom extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private ChatRoomType type;
}
