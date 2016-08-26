package blog.forms;

import blog.models.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;


public class PostForm {

    private Long id;

    @Size(min = 2, max = 300, message = "Please insert title btn 2 i 30")
    private String title;


    private String body;


    private Long author;


    private String date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getAuthor() {
        return author;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public PostForm() {
    }

    public PostForm(String title, String body, Long author, String date) {
        this.title = title;
        this.body = body;
        this.date = date;
    }

    public PostForm(Long id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }
}
