package com.rkisuru.blog.mapper;

import com.rkisuru.blog.entity.Comment;
import com.rkisuru.blog.request.CommentRequest;
import org.springframework.stereotype.Service;

@Service
public class CommentMapper {

    public Comment toComment(CommentRequest request) {

        return Comment.builder()
                .content(request.content())
                .build();
    }
}
