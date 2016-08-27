package blog.services;

import blog.models.Comment;

import java.util.List;


public interface CommentService {
    List<Comment> findAllByAuthorId(Long id);
    List<Comment> findAllByPostId(Long id);
    Comment create(Comment comment);
    void deleteById(Long id);
}
