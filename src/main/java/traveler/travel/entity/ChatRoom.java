package traveler.travel.entity;

import traveler.travel.ChatRoomType;

import javax.persistence.*;

@Entity
public class ChatRoom extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private ChatRoomType type;
}
