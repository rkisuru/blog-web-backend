package com.rkisuru.blog.controller;

import com.rkisuru.blog.request.CommentRequest;
import com.rkisuru.blog.service.CommentService;
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
    public ResponseEntity<?> createComment(@PathVariable Long postId, @RequestBody CommentRequest commentRequest){
        return ResponseEntity.ok(commentService.createComment(postId, commentRequest));
    }

    @PutMapping("/{postId}/{commentId}")
    public ResponseEntity<?> editComment(@PathVariable Long commentId, @RequestBody String content, Authentication connectedUser) {
        return ResponseEntity.ok(commentService.editComment(commentId, content, connectedUser));
    }

    @DeleteMapping("/{postId}/{comment_id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long comment_id, Authentication connectedUser) {
        return ResponseEntity.ok(commentService.deleteComment(comment_id, connectedUser));
    }

    @GetMapping("comments/{postId}")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable Long postId){
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }
}
