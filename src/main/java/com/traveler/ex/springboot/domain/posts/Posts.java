package com.traveler.ex.springboot.domain.posts;

import com.traveler.ex.springboot.domain.BaseTimeEntity;
import com.traveler.ex.springboot.domain.category.Category;
import com.traveler.ex.springboot.domain.comment.Comment;
import com.traveler.ex.springboot.domain.like.Like;
import com.traveler.ex.springboot.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class Posts extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "posts_id")
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private String writer;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.REMOVE)
    @OrderBy("id asc") //댓글 정렬
    private List<Comment> comments = new ArrayList<>();


    @OneToMany(mappedBy = "posts", orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


    //연관관계 편의 메서드


    /*게시글 수정*/
    public void update(String title, String content) {

        this.title = title;
        this.content = content;
    }

}
