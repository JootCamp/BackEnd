package com.jootcamp.superboard.post.service;

import com.jootcamp.superboard.post.repository.PostRepository;
import com.jootcamp.superboard.post.repository.entity.PostEntity;
import com.jootcamp.superboard.post.repository.execption.PostNotFoundException;
import com.jootcamp.superboard.post.service.dto.Post;
import com.jootcamp.superboard.post.service.dto.UpsertPost;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post create(UpsertPost upsertPost) {
        PostEntity post = postRepository.save(upsertPost.toEntity());
        return Post.from(post);
    }

    public List<Post> findAll() {
        List<PostEntity> posts = postRepository.findAllByIsDeletedIsFalse();
        return posts.stream()
                .map(Post::from)
                .toList();
    }

    public Post findById(long postId) {
        PostEntity post = postRepository.findByIdAndIsDeletedIsFalse(postId)
                .orElseThrow(()->new PostNotFoundException(postId));

        return Post.from(post);
    }

    @Transactional
    public void delete(long userId, long postId) {
        PostEntity post = postRepository.findByIdAndIsDeletedIsFalse(postId)
                .orElseThrow(()-> new PostNotFoundException(postId));

        post.delete(userId);
    }

}
