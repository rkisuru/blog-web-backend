package com.rkisuru.blog.request;

import jakarta.validation.constraints.NotNull;

public record CommentEditRequest(

        @NotNull
        String content
) {
}
