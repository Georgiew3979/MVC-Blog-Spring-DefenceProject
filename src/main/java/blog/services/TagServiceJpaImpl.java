package blog.services;

import blog.models.Comment;
import blog.models.Post_tags;
import blog.models.Tag;
import blog.repositories.CommentRepository;
import blog.repositories.TagNameRepository;
import blog.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceJpaImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagNameRepository tagNameRepository;

    @Override
    public List<Tag> findAll() {
        return this.tagNameRepository.findAll();
    }

    @Override
    public List<Post_tags> findAllByTagId(Long id) {
        return this.tagRepository.findAllByTagId(id);
    }

    @Override
    public Tag findById(Long id) {
        return this.tagNameRepository.findOne(id);
    }

    @Override
    public List<Post_tags> findAllByPostId(Long id) {
        return this.tagRepository.findAllByPostId(id);
    }

    @Override
    public Tag create(Tag tag) {
        return this.tagNameRepository.save(tag);
    }

    @Override
    public Post_tags create(Post_tags post_tags) {
        return this.tagRepository.save(post_tags);
    }

    @Override
    public void deleteTagById(Long id) {
        this.tagNameRepository.delete(id);

    }

    @Override
    public void deletePost_tagsById(Long id) {
        this.tagRepository.delete(id);

    }
}
