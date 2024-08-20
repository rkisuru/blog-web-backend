package com.rkisuru.blog.controller;

import com.rkisuru.blog.entity.Post;
import com.rkisuru.blog.request.PostRequest;
import com.rkisuru.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequest request){
        return ResponseEntity.ok(postService.savePost(request));
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(){
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId, Authentication connectedUser){
        return ResponseEntity.ok(postService.getPostById(postId, connectedUser));
    }

    @PutMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable Long postId, Authentication connectedUser){
        return ResponseEntity.ok(postService.likePost(postId, connectedUser));
    }

    @DeleteMapping("/{postId}/delete")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, Authentication connectedUser){
        return ResponseEntity.ok(postService.deletePost(postId, connectedUser));
    }

    @PutMapping("/{postId}/edit")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody Post post, Authentication connectedUser){
        return ResponseEntity.ok(postService.editPost(postId, post, connectedUser));
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<?> searchByName(@PathVariable String name){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(postService.searchByName(name));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
