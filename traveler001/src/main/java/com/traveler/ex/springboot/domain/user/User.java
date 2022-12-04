package com.traveler.ex.springboot.domain.user;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "user_id")
    private Long id;

    private String nickname;


}
