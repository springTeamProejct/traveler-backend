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
    private LocalDateTime time_date;    //LocalDate
    private String method;     //enum타입으로
    private String gender;    //enum type으로
    private int minAge;
    private int maxAge;




}
