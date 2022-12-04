package com.traveler.ex.springboot.domain.comment;

import com.traveler.ex.springboot.domain.posts.Posts;
import com.traveler.ex.springboot.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 부모댓글, 셀프조인
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment; //댓글 내용

    @Column(name="created_date")
    @CreatedDate
    private String createdDate;

    @Column(name="modified_date")
    @LastModifiedDate
    private String modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true) //부모엔티티에서 자식엔티티제거 ->자식제거고아객체
    private List<Comment> children = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "posts_id")
    private Posts posts;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;




    /*댓글 수정*/
    public void update(String comment) {
        this.comment = comment;
    }

}
