package com.rkisuru.blog.response;

import com.rkisuru.blog.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {

    private long id;
    private String title;
    private String content;
    private byte[] coverImage;
    private String createdBy;
    private LocalDateTime createdDate;
    private int likeCount;
    private int viewCount;
    private List<String> tags;
    private List<Comment> comments;
}
