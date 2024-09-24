package com.rkisuru.blog.request;

import com.rkisuru.blog.type.PostType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PostRequest(

        @NotNull
        @NotEmpty
        String title,

        @NotNull
        @NotEmpty
        String content,

        @NotNull
        PostType type,

        List<String> tags
) {
}
