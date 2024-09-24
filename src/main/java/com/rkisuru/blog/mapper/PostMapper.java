package com.rkisuru.blog.mapper;

import com.rkisuru.blog.entity.Post;
import com.rkisuru.blog.request.PostRequest;
import com.rkisuru.blog.response.PostResponse;
import com.rkisuru.blog.service.ReadFileFromLocation;
import org.springframework.stereotype.Service;

@Service
public class PostMapper {

    public Post toPost(PostRequest request) {

        return Post.builder()
                .title(request.title())
                .content(request.content())
                .postType(request.type())
                .tags(request.tags())
                .build();
    }

    public PostResponse fromPost(Post post) {

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .type(post.getPostType())
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .tags(post.getTags())
                .viewCount(post.getViewCount())
                .createdBy(post.getPostedBy())
                .createdDate(post.getPostedAt())
                .coverImage(ReadFileFromLocation.readFile(post.getCover()))
                .build();
    }
}
