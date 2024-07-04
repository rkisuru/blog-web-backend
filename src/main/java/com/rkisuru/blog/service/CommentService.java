package com.rkisuru.blog.service;

import com.rkisuru.blog.entity.Comment;

public interface CommentService {
    Comment createComment(Long postId, String postedBy, String content);
}
