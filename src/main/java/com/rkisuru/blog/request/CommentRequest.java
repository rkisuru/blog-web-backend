package com.rkisuru.blog.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(

        Long id,

        @NotNull
        @NotEmpty
        String content
) {
}
