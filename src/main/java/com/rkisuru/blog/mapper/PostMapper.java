package com.rkisuru.blog.mapper;

import com.rkisuru.blog.entity.Post;
import com.rkisuru.blog.request.PostRequest;
import org.springframework.stereotype.Service;

@Service
public class PostMapper {

    public Post toPost(PostRequest request) {

        return Post.builder()
                .title(request.title())
                .content(request.content())
                .cover(request.cover())
                .tags(request.tags())
                .build();
    }
}
