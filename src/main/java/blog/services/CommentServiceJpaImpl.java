package blog.services;

import blog.models.Comment;
import blog.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceJpaImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> findAllByAuthorId(Long id) {
        return this.commentRepository.findByAuthorId(id);
    }

    @Override
    public List<Comment> findAllByPostId(Long id) {
        return this.commentRepository.findByPostId(id);
    }

    @Override
    public Comment create(Comment comment) {
        return this.commentRepository.save(comment);
    }

    @Override
    public void deleteById(Long id) {
        this.commentRepository.delete(id);
    }

    @Override
    public List<Comment> findAll() {
        return this.commentRepository.findAll();
    }
}
