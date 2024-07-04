package com.rkisuru.blog.service;

import com.rkisuru.blog.entity.Post;

import java.util.List;

public interface PostService {
    Post savePost(Post post);
    List<Post> getAllPosts();
    Post getPostById(Long postId);
}


