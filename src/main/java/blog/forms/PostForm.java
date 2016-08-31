package blog.forms;

import blog.models.Tag;
import blog.models.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;


public class PostForm {

    private Long id;

    @Size(min = 2, max = 300, message = "Please insert title btn 2 i 30")
    private String title;


    private String body;


    private Long author;

    private String authorName;

    private String dateNew;

    private Date date =  new Date();

    private List<Tag> tags;

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getDateNew() {
        return dateNew;
    }

    public void setDateNew(String dateNew) {
        this.dateNew = dateNew;
    }

    public Date getDate() {
        return date;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setDate(Date date) {
        this.date = date;
    }

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


    public PostForm() {
    }

    public PostForm(String title, String body, String authorName, String dateNew) {
        this.title = title;
        this.body = body;
        this.authorName = authorName;
        this.dateNew = dateNew;
    }

    public PostForm(String title, String body, String authorName, Date date) {
        this.title = title;
        this.body = body;
        this.authorName = authorName;
        this.date = date;
    }

    public PostForm(String title, String body, Long author, Date date) {
        this.title = title;
        this.body = body;
        this.author = author;
        this.date = date;
    }

    public PostForm(Long id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }
}
