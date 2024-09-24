package com.rkisuru.blog.controller;

import com.rkisuru.blog.request.CommentEditRequest;
import com.rkisuru.blog.request.CommentRequest;
import com.rkisuru.blog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/posts")
@CrossOrigin
@RequiredArgsConstructor
public class CommentController {


    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<?> createComment(@PathVariable Long postId, @Valid @RequestBody CommentRequest commentRequest) throws Exception {
        return ResponseEntity.ok(commentService.createComment(postId, commentRequest));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> editComment(@PathVariable Long commentId, @RequestBody CommentEditRequest request, Authentication connectedUser) {
        return ResponseEntity.ok(commentService.editComment(commentId, request, connectedUser));
    }

    @DeleteMapping("/{comment_id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long comment_id, Authentication connectedUser) {
        return ResponseEntity.ok(commentService.deleteComment(comment_id, connectedUser));
    }

    @GetMapping("comments/{postId}")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable Long postId){
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }
}
