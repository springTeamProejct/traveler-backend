package traveler.travel.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Post extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;
    @Lob
    private String content;
    private int viewCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

}
