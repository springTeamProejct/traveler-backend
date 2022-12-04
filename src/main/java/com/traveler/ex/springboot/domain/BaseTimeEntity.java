package com.traveler.ex.springboot.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


/**
 * 공통적으로 사용되는 컬럼이므로, 이를 상속한 클래스에서 컬럼을 추가.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @Column(name= "created_date", nullable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name= "modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
