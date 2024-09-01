package com.rkisuru.blog.request;

import java.util.List;

public record EditRequest(
        String title,
        String content,
        List<String> tags
) {
}
