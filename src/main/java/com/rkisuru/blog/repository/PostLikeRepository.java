package com.rkisuru.blog.repository;

import com.rkisuru.blog.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {

    @Query("SELECT postlike FROM PostLike postlike WHERE postlike.likedBy = :likedBy AND postlike.post.id = :postId")
    Optional<PostLike> findPostLikeById(@Param("likedBy") String likedBy, @Param("postId") Long postId);
}
