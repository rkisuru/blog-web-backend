package com.rkisuru.blog.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(

        @NotNull(message = "Enter the comment")
        @NotEmpty(message = "Enter the comment")
        String content
) {
}
