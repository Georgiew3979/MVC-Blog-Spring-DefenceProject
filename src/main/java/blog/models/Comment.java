package blog.models;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String content;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User author;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Post post;

    @Column(nullable = false)
    private Date date = new Date();

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Comment() {
    }

    public Comment(String content, User author, Post post, Date date) {
        this.content = content;
        this.author = author;
        this.post = post;
        this.date = date;
    }
}
