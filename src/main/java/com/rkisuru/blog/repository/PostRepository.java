package com.rkisuru.blog.repository;

import com.rkisuru.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT posts FROM Post posts WHERE posts.title LIKE %:title%")
    List<Post> findAllByTitle(@Param("title") String title);

    List<Post> findByTags(List<String> tags);
}
