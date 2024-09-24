package com.rkisuru.blog.request;

import com.rkisuru.blog.type.PostType;

import java.util.List;

public record EditRequest(
        String title,
        String content,
        PostType type,
        List<String> tags
) {
}
