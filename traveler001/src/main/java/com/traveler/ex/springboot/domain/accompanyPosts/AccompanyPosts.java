package com.traveler.ex.springboot.domain.accompanyPosts;

import com.traveler.ex.springboot.domain.posts.Posts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccompanyPosts extends Posts {

    private String place;
    private String gather_yn;
    private LocalDateTime time_date;
    private String method;
    private String gender;
    private int minAge;
    private int maxAge;




}
