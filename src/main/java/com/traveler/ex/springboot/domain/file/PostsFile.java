package com.traveler.ex.springboot.domain.file;

import com.traveler.ex.springboot.domain.posts.Posts;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class PostsFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "posts_file_id")
    private Long id;            //번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id" )
    private Posts posts;
    private String delYn;

    @OneToOne
    @JoinColumn(name = "file_id")
    private File file;

    @Builder
    public PostsFile(Posts posts, Long fileId, String delYn, File file){
        this.posts = posts;
        this.delYn = "N";
        this.file = file;
    }
}
