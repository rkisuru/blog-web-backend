package com.rkisuru.blog.service;

import com.rkisuru.blog.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(Long postId, String postedBy, String content);
    public List<Comment> getCommentsByPostId(Long postId);
}
