package blog.services;

import blog.models.Post;

import java.util.List;

public interface PostService {
    List<Post> findAll();
    List<Post> findLatest5();
    List<Post> findNext5(int page, int size);
    Post findById(Long id);
    Post create(Post post);
    Post edit(Post post);
    void deleteById(Long id);
    Post register(String title, String body);

}
