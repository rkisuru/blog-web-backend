package com.rkisuru.blog.repository;

import com.rkisuru.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByTitle(String name);
    List<Post> findByTags(List<String> tags);
}
