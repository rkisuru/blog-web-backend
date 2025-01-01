package com.rkisuru.blog.service;

import com.rkisuru.blog.entity.Comment;
import com.rkisuru.blog.entity.Post;
import com.rkisuru.blog.entity.PostLike;
import com.rkisuru.blog.exception.OperationNotPermittedException;
import com.rkisuru.blog.mapper.PostMapper;
import com.rkisuru.blog.repository.CommentRepository;
import com.rkisuru.blog.repository.PostLikeRepository;
import com.rkisuru.blog.repository.PostRepository;
import com.rkisuru.blog.request.EditRequest;
import com.rkisuru.blog.request.PostRequest;
import com.rkisuru.blog.response.PostResponse;
import com.rkisuru.blog.type.PostType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper mapper;
    private final PostLikeRepository postLikeRepository;
    private final FileUploadService fileUploadService;
    private final CommentRepository commentRepository;

    public Long savePost(PostRequest request){

        Post post = mapper.toPost(request);
        post.setLikeCount(0);
        post.setViewCount(0);
        return postRepository.save(post).getId();
    }

    public List<PostResponse> getAllPosts(){
        return postRepository.findAll().stream()
                .map(mapper::fromPost)
                .toList();
    }

    public PostResponse getPostById(Long postId, @AuthenticationPrincipal OAuth2User user){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

            if (!post.getPostedBy().equals(user.getAttribute("sub"))){
                post.setViewCount(post.getViewCount()+1);
                postRepository.save(post);
            }

        return mapper.fromPost(post);
    }


    public Post likePost(Long postId, @AuthenticationPrincipal OAuth2User user){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Optional<PostLike> optPostLike = postLikeRepository.findPostLikeById(user.getAttribute("sub"), postId);
        if (optPostLike.isPresent()){
            PostLike postLike = optPostLike.get();
            post.setLikeCount(post.getLikeCount()-1);
            postLikeRepository.delete(postLike);
        } else {
            PostLike postLike = new PostLike();
            post.setLikeCount(post.getLikeCount()+1);
            postLike.setLikedBy(user.getAttribute("sub"));
            postLike.setPost(post);
            postLikeRepository.save(postLike);
        }
        return post;
    }

    public List<PostResponse> searchByTitle(String title){
        return postRepository.findAllByTitle(title)
                .stream()
                .map(mapper::fromPost)
                .toList();
    }

    public void deletePost(Long postId, @AuthenticationPrincipal OAuth2User user){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id:"+postId));

        List<Comment> postComments = commentRepository.findByPostId(postId);
        List<PostLike> postLikes = postLikeRepository.findPostLikeByPostId(postId);

            if (post.getPostedBy().equals(user.getAttribute("sub"))){
                commentRepository.deleteAll(postComments);
                postLikeRepository.deleteAll(postLikes);
                postRepository.delete(post);
            }
            throw new OperationNotPermittedException("You are not allowed to delete this post");
    }

    public Post editPost(Long postId, EditRequest request, @AuthenticationPrincipal OAuth2User user){

        Post Opost = postRepository.findById(postId)
                .orElseThrow(()-> new EntityNotFoundException("Post not found"));

            if (Opost.getPostedBy().equals(user.getAttribute("sub"))){
                if (!request.title().isBlank()) {
                    Opost.setTitle(request.title());
                }
                if (request.type() != null) {
                    Opost.setPostType(request.type());
                }
                if (!request.content().isBlank()) {
                    Opost.setContent(request.content());
                }
                if (!request.tags().isEmpty()) {
                    Opost.setTags(request.tags());
                }
                postRepository.save(Opost);
                return Opost;
            }
            throw new OperationNotPermittedException("You are not allowed to edit this post");
    }

    public void uploadPostCover(Long postId, MultipartFile file, @AuthenticationPrincipal OAuth2User user) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        if (post.getPostedBy().equals(user.getAttribute("sub"))){
            var postCover = fileUploadService.uploadFile(file);
            post.setCover(postCover);
            postRepository.save(post);
        }
    }

    public void updatePostCover(Long postId, MultipartFile file, @AuthenticationPrincipal OAuth2User user) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        if (post.getPostedBy().equals(user.getAttribute("sub"))){
            var postCover = fileUploadService.uploadFile(file);
            post.setCover(postCover);
            postRepository.save(post);
        }
    }

    public List<PostResponse> findPostsByUser(@AuthenticationPrincipal OAuth2User user) {

        return postRepository.findPostsByPostedBy(user.getAttribute("sub"))
                .stream()
                .map(mapper::fromPost)
                .toList();

    }

    public List<PostResponse> filterByType(PostType type) {

        return postRepository.findPostsByPostType(type)
                .stream()
                .map(mapper::fromPost)
                .toList();

    }
}
