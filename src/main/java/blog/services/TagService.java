package blog.services;


import blog.models.Tag;
import blog.models.Post_tags;
import java.util.List;


public interface TagService {
    List<Post_tags> findAllByTagId(Long id);
    List<Post_tags> findAllByPostId(Long id);
    Tag create(Tag tag);
    Post_tags create(Post_tags post_tags);
    void deleteTagById(Long id);
    void deletePost_tagsById(Long id);
    List<Tag> findAll();
    Tag findById(Long id);
}
