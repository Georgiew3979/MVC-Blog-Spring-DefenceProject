package blog.repositories;

import blog.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long > {
    List<Comment> findByAuthorId(Long id);
    List<Comment> findByPostId(Long id);
}
