package blog.models;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "post_tags")
public class Post_tags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Tag tag;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Post post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Post_tags() {
    }

    public Post_tags(Tag tag, Post post) {
        this.tag = tag;
        this.post = post;
    }
}
