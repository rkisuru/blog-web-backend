package com.rkisuru.blog.controller;

import com.rkisuru.blog.request.EditRequest;
import com.rkisuru.blog.request.PostRequest;
import com.rkisuru.blog.response.PostResponse;
import com.rkisuru.blog.service.PostService;
import com.rkisuru.blog.type.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody PostRequest request){
        return ResponseEntity.ok(postService.savePost(request));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(){
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
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody EditRequest request, Authentication connectedUser){
        return ResponseEntity.ok(postService.editPost(postId, request, connectedUser));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByTitle(@RequestParam String title){
        return ResponseEntity.ok(postService.searchByTitle("%"+title+"%"));
    }

    @PostMapping(value = "/cover/{postId}", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadPostCover(@RequestParam("file") MultipartFile file, @PathVariable Long postId, Authentication connectedUser) throws IOException {
        postService.uploadPostCover(postId, file, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{postId}/edit/cover", consumes = "multipart/form-data")
    public ResponseEntity<?> updatePostCover(@RequestParam("file") MultipartFile file, @PathVariable Long postId, Authentication connectedUser) throws IOException {
        postService.updatePostCover(postId, file, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<PostResponse>> findAllPostsByUser(Authentication connectedUser){
        return ResponseEntity.ok(postService.findPostsByUser(connectedUser));
    }

    @GetMapping("/filter/{postType}")
    public ResponseEntity<?> findPostsByPostType(@PathVariable PostType postType){
        return ResponseEntity.ok(postService.filterByType(postType));
    }
}
