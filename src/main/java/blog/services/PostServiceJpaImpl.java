package blog.services;

import blog.models.Post;
import blog.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class PostServiceJpaImpl implements PostService {
    @Autowired
    public PostRepository postRepository;

    @Override
    public List<Post> findAll() {
        return this.postRepository.findAll();
    }

    @Override
    public List<Post> findLatest5() {
        return this.postRepository.findLatestPosts(new PageRequest(0,5));
    }

    @Override
    public List<Post> findNext5(int page, int size) {
        return this.postRepository.findLatestPosts(new PageRequest(page,size));
    }

    @Override
    public Post findById(Long id) {
        return this.postRepository.findOne(id);
    }

    @Override
    public Post create(Post post) {
        return this.postRepository.save(post);
    }

    @Override
    public Post edit(Post post) {
        return this.postRepository.save(post);
    }

    @Override
    public Post register(String title, String body) {
        Post post = new Post(title, body);
        return post;
    }

    @Override
    public void deleteById(Long id) {
            this.postRepository.delete(id);
    }
}
