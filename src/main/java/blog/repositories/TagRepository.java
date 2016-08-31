package blog.repositories;

import blog.models.Comment;
import blog.models.Post_tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Post_tags, Long > {
    List<Post_tags> findAllByTagId(Long id);
    List<Post_tags> findAllByPostId(Long id);
}

