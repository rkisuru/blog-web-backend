package com.rkisuru.blog.service;

import com.rkisuru.blog.entity.Post;
import com.rkisuru.blog.mapper.PostMapper;
import com.rkisuru.blog.repository.PostRepository;
import com.rkisuru.blog.request.EditRequest;
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

            post.setLikeCount(post.getLikeCount()+1);
            return postRepository.save(post);
    }

    public List<Post> searchByTitle(String title){
        return postRepository.findAllByTitle(title);
    }

    public String deletePost(Long postId, Authentication connectedUser){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id:"+postId));

            if (post.getPostedBy().equals(connectedUser.getName())){
                postRepository.delete(post);
            }
            return "Post Deleted Successfully";
    }

    public Post editPost(Long postId, EditRequest request, Authentication connectedUser){

        Post Opost = postRepository.findById(postId)
                .orElseThrow(()-> new EntityNotFoundException("Post not found"));

            if (Opost.getPostedBy().equals(connectedUser.getName())){
                Opost.setTitle(request.title());
                Opost.setContent(request.content());
                Opost.setCover(request.cover());
                Opost.setTags(request.tags());
                return postRepository.save(Opost);
            }
       return Opost;
    }
}
