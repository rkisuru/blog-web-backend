package com.rkisuru.blog.service;

import com.rkisuru.blog.entity.Comment;
import com.rkisuru.blog.entity.Post;
import com.rkisuru.blog.mapper.CommentMapper;
import com.rkisuru.blog.repository.CommentRepository;
import com.rkisuru.blog.repository.PostRepository;
import com.rkisuru.blog.request.CommentRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper mapper;

    private final PostRepository postRepository;

    public Comment createComment(Long postId, CommentRequest request) {

        Optional<Post> optionalPost = postRepository.findById(postId);

        if(optionalPost.isPresent()){
            Comment comment = mapper.toComment(request);
            return commentRepository.save(comment);
        }
            throw new EntityNotFoundException("Post not found");
    }

    public Comment editComment(Long commentId, String content, Authentication connectedUser) throws IllegalAccessException {

        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if(optionalComment.isPresent()){
            Comment comment = optionalComment.get();
            if (comment.getPostedBy().equals(connectedUser.getName())) {
                comment.setContent(content);
                return commentRepository.save(comment);
            }
            throw new IllegalAccessException("You are not authorized to edit this comment");
        }
        throw new EntityNotFoundException("Comment not found");
    }

    public String deleteComment(Long commentId, Authentication connectedUser) throws IllegalAccessException {

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if(optionalComment.isPresent()){
            Comment comment = optionalComment.get();
            if (comment.getPostedBy().equals(connectedUser.getName())) {
                commentRepository.delete(comment);
                return "Comment deleted successfully";
            }
            throw new IllegalAccessException("You are not authorized to edit this comment");
        }
        throw new EntityNotFoundException("Comment not found");
    }

    public List<Comment> getCommentsByPostId(Long postId){
        return commentRepository.findByPostId(postId);
    }
}
