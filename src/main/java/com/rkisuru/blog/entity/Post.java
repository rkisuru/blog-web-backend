package com.rkisuru.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("JpaAttributeTypeInspection")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @CreatedBy
    @Column(updatable = false, nullable = false)
    private String postedBy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime postedAt;

    @LastModifiedBy
    @Column(insertable = false)
    private String lastModifiedBy;

    @Column(nullable = false)
    private String cover;

    private int likeCount;
    private int viewCount;

    private List<String> tags;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

}
