package traveler.travel.domain.chat.entity;

import traveler.travel.domain.chat.enums.ChatRoomType;
import traveler.travel.domain.common.entity.BaseTimeEntity;

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
