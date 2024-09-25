package com.rkisuru.blog.service;

import com.rkisuru.blog.entity.Comment;
import com.rkisuru.blog.entity.Post;
import com.rkisuru.blog.exception.OperationNotPermittedException;
import com.rkisuru.blog.mapper.CommentMapper;
import com.rkisuru.blog.repository.CommentRepository;
import com.rkisuru.blog.repository.PostRepository;
import com.rkisuru.blog.request.CommentEditRequest;
import com.rkisuru.blog.request.CommentRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper mapper;

    private final PostRepository postRepository;

    public Comment createComment(Long postId, CommentRequest request) throws Exception {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Comment comment = mapper.toComment(request);

        if (!comment.getContent().isBlank()) {
            comment.setPost(post);
            post.getComments().add(comment);
            return commentRepository.save(comment);
        }
        throw new Exception("Comment cannot be empty!");
    }

    public Comment editComment(Long commentId, CommentEditRequest request, Authentication connectedUser) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

            if (comment.getPostedBy().equals(connectedUser.getName())) {

                if (!request.content().isBlank()) {
                    comment.setContent(request.content());
                    return commentRepository.save(comment);
                }
                return commentRepository.save(comment);
            }
            throw new OperationNotPermittedException("You do not have permission to edit this comment");
    }

    public String deleteComment(Long commentId, Authentication connectedUser) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

            if (comment.getPostedBy().equals(connectedUser.getName())) {
                commentRepository.delete(comment);
                return "Comment deleted successfully";
            }
            throw new OperationNotPermittedException("You do not have permission to delete this comment");
    }

    public List<Comment> getCommentsByPostId(Long postId){
        return commentRepository.findByPostId(postId);
    }
}
