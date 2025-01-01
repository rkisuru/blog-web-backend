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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    public Comment editComment(Long commentId, CommentEditRequest request, @AuthenticationPrincipal OAuth2User user) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

            if (comment.getPostedBy().equals(user.getAttribute("sub"))) {

                if (!request.content().isBlank()) {
                    comment.setContent(request.content());
                    return commentRepository.save(comment);
                }
                return commentRepository.save(comment);
            }
            throw new OperationNotPermittedException("You do not have permission to edit this comment");
    }

    public void deleteComment(Long commentId, @AuthenticationPrincipal OAuth2User user) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

            if (comment.getPostedBy().equals(user.getAttribute("sub"))) {
                commentRepository.delete(comment);
            }
            throw new OperationNotPermittedException("You do not have permission to delete this comment");
    }

    public List<Comment> getCommentsByPostId(Long postId){
        return commentRepository.findByPostId(postId);
    }
}
