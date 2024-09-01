package com.rkisuru.blog.request;

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

        List<String> tags
) {
}
