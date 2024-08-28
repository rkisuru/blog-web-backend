package com.rkisuru.blog.service;

import com.rkisuru.blog.entity.Post;
import com.rkisuru.blog.entity.PostLike;
import com.rkisuru.blog.mapper.PostMapper;
import com.rkisuru.blog.repository.PostLikeRepository;
import com.rkisuru.blog.repository.PostRepository;
import com.rkisuru.blog.request.EditRequest;
import com.rkisuru.blog.request.PostRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper mapper;
    private final PostLikeRepository postLikeRepository;

    public Long savePost(PostRequest request){

        Post post = mapper.toPost(request);
        post.setLikeCount(0);
        post.setViewCount(0);
        return postRepository.save(post).getId();
    }

    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    public Post getPostById(Long postId, Authentication connectedUser){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

            if (!post.getPostedBy().equals(connectedUser.getName())){
                post.setViewCount(post.getViewCount()+1);
                return postRepository.save(post);
            }

        return post;
    }


    public Post likePost(Long postId, Authentication connectedUser){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Optional<PostLike> optPostLike = postLikeRepository.findPostLikeById(connectedUser.getName(), postId);
        if (optPostLike.isPresent()){
            PostLike postLike = optPostLike.get();
            post.setLikeCount(post.getLikeCount()-1);
            postLikeRepository.delete(postLike);
        } else {
            PostLike postLike = new PostLike();
            post.setLikeCount(post.getLikeCount()+1);
            postLike.setLikedBy(connectedUser.getName());
            postLike.setPost(post);
            postLikeRepository.save(postLike);
        }
        return post;
    }

    public List<Post> searchByTitle(String title){
        return postRepository.findAllByTitle(title);
    }

    public String deletePost(Long postId, Authentication connectedUser){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id:"+postId));

            if (post.getPostedBy().equals(connectedUser.getName())){
                postRepository.delete(post);
                return "Post Deleted Successfully";
            }
            throw new AccessDeniedException("Access denied");
    }

    public Post editPost(Long postId, EditRequest request, Authentication connectedUser){

        Post Opost = postRepository.findById(postId)
                .orElseThrow(()-> new EntityNotFoundException("Post not found"));

            if (Opost.getPostedBy().equals(connectedUser.getName())){
                if (request.title() != null) {
                    Opost.setTitle(request.title());
                }
                if (request.content() != null) {
                    Opost.setContent(request.content());
                }
                if (request.cover() != null) {
                    Opost.setCover(request.cover());
                }
                if (request.tags() != null) {
                    Opost.setTags(request.tags());
                }
                postRepository.save(Opost);
                return Opost;
            }
            throw new AccessDeniedException("Access denied");
    }
}
