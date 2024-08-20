package com.rkisuru.blog.service;

import com.rkisuru.blog.entity.Post;
import com.rkisuru.blog.mapper.PostMapper;
import com.rkisuru.blog.repository.PostRepository;
import com.rkisuru.blog.request.PostRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper mapper;

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
        Optional<Post> optionalPost = postRepository.findById(postId);
        if(optionalPost.isPresent()){
            Post post = optionalPost.get();
            if (!post.getPostedBy().equals(connectedUser.getName())){
                post.setViewCount(post.getViewCount()+1);
                return postRepository.save(post);
            }
        } throw new EntityNotFoundException("Post Not Found");
    }


    public Post likePost(Long postId, Authentication connectedUser){
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()){
            Post post = optionalPost.get();
            post.setLikeCount(post.getLikeCount()+1);
            return postRepository.save(post);
        }
            throw new EntityNotFoundException("Post not found with id:"+postId);
    }

    public List<Post> searchByName(String name){
        return postRepository.findAllByTitle(name);
    }

    public String deletePost(Long postId, Authentication connectedUser){

        Optional<Post> optionalPost = postRepository.findById(postId);
        if(optionalPost.isPresent()){
            Post post = optionalPost.get();
            if (post.getPostedBy().equals(connectedUser.getName())){
                postRepository.delete(post);
            }
            return "Post Deleted Successfully";
        } throw new EntityNotFoundException("Post not found with id:"+postId);
    }

    public Post editPost(Long postId, Post post, Authentication connectedUser){

        Optional<Post> optionalPost = postRepository.findById(postId);
        if(optionalPost.isPresent()){
            Post Opost = optionalPost.get();
            if (Opost.getPostedBy().equals(connectedUser.getName())){
                Opost.setTitle(post.getTitle());
                Opost.setContent(post.getContent());
                Opost.setCover(post.getCover());
                return postRepository.save(Opost);
            }
        } throw new EntityNotFoundException("Post not found with id:"+postId);
    }
}
