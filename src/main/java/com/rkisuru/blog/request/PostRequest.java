package com.rkisuru.blog.request;

import com.rkisuru.blog.type.PostType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PostRequest(

        @NotNull(message = "Title is required")
        @NotEmpty(message = "Title is required")
        String title,

        @NotNull(message = "Content is required")
        @NotEmpty(message = "Content is required")
        String content,

        @NotNull
        PostType type,

        List<String> tags
) {
}
