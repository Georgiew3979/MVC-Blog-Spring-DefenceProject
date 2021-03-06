package blog.repositories;

import blog.models.Post_tags;
import blog.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagNameRepository extends JpaRepository<Tag, Long > {

}

