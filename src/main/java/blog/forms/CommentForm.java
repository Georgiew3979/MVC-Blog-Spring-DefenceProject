package blog.forms;

import javax.validation.constraints.Size;


public class CommentForm {

    private Long id;

    @Size(min = 2, max = 300, message = "Please insert title btn 2 i 300")
    private String content;


    private Long author;


    private Long post;


    private String date;

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

    public Long getAuthor() {
        return author;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }

    public Long getPost() {
        return post;
    }

    public void setPost(Long post) {
        this.post = post;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public CommentForm(Long id, String content, Long author, Long post, String date) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.post = post;
        this.date = date;
    }

    public CommentForm() {
    }
}
