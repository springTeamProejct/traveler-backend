package traveler.travel.domain.post.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.travel.domain.common.entity.BaseTimeEntity;
import traveler.travel.domain.account.entity.User;
import traveler.travel.domain.post.enums.Category;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;
    @Lob
    private String content;
    private int viewCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToOne(fetch = FetchType.LAZY)
    private Travel travel;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    public void setTravel(Travel travel) {
        this.travel = travel;
    }

    @Builder
    public Post(String title, String content, User writer, Category category, Travel travel) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.category = category;
        this.travel = travel;

        viewCnt = 0;
    }

    public void view() {
        viewCnt += 1;
    }

    public void update(String title, String content, Travel travel) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
        if (travel != null) this.travel = travel;
    }
}
