package blog.controllers;

import blog.forms.CommentForm;
import blog.forms.PostForm;
import blog.models.Comment;
import blog.models.Post;
import blog.models.User;
import blog.services.CommentService;
import blog.services.NotificationService;
import blog.services.PostService;
import blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;
import java.util.Collections;
import java.util.List;
import java.util.Date;

import static java.util.Collections.reverse;


@Controller
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @Autowired
    private NotificationService notificationService;


    @RequestMapping("/posts")
    public String viewAllPost(Model model) {
        List<Post> posts = postService.findAll();
        if(posts == null) {
            notificationService.addErrorMessage("Cannot find any post");
            return "redirect:/";
        }

        for (Post post: posts) {
            post.setBody(cutLongText(post.getBody()));
        }
        Collections.reverse(posts);
        model.addAttribute("posts", posts);
        return "posts/index";
    }

    private String cutLongText(String text)
    {
        int maxSize = 200;
        String cutedText = text.substring(0, Math.min(text.length(),maxSize));
        if (text.length() > maxSize) {
              text = cutedText + "...";
        }
        return text;
    }

    @RequestMapping("/posts/delete/{id}")
    public String deletePost(@PathVariable("id") Long id, Model model) {
        Post post = postService.findById(id);
        if(post == null) {
            notificationService.addErrorMessage("Cannot find  pos:" + id);
            return "redirect:/posts";
        }

        model.addAttribute("post", post);
        return "posts/delete";
    }

    @RequestMapping(value = "/posts/delete/{id}", method = RequestMethod.POST)
    public String deletePostById(@PathVariable("id") Long id) {
        Post post = postService.findById(id);
        if(post == null) {
            notificationService.addErrorMessage("Cannot find  pos:" + id);
            return "redirect:/posts";
        }
        // delete comment first
        List<Comment> comments = commentService.findAllByPostId(id);
        if (!comments.isEmpty()) {
            for (Comment comment : comments) {
                commentService.deleteById(comment.getId());
            }
        }
        postService.deleteById(id);
        if(postService.findById(id) == null) {
            notificationService.addInfoMessage("The post was deleted");
            return "redirect:/posts";
        } else {
            notificationService.addErrorMessage("Cannot delete  pos:" + id);
        }
        return "posts/delete";
    }

    @RequestMapping("/posts/edit/{id}")
    public String editPost(@PathVariable("id") Long id, Model model) {
        Post post = postService.findById(id);
        if(post == null) {
            notificationService.addErrorMessage("Cannot find  pos:" + id);
            return "redirect:/posts";
        }

        model.addAttribute("post", post);
        return "posts/edit";
    }

    @RequestMapping(value = "/posts/edit/{id}", method = RequestMethod.POST)
    public String editPost(@PathVariable("id") Long id, @Valid PostForm postForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            notificationService.addErrorMessage("Please fill all fields");
            return "posts/edit";
        }
        Post post = postService.findById(id);
        post.setTitle(postForm.getTitle());
        post.setBody(postForm.getBody());


        User author = userService.findByUsername(postForm.getAuthorName());
        post.setAuthor(author);

        Date date = new Date();
        post.setDate(date);

        postService.edit(post);

        notificationService.addInfoMessage("Post editted, Follow next step");
        return "redirect:/posts";

    }

    @RequestMapping("/posts/create")
    public String showPostForm (PostForm postForm) {
        return "posts/create";
    }

    @RequestMapping(value = "/posts/create", method = RequestMethod.POST)
    public String createPost (@Valid PostForm postForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            notificationService.addErrorMessage("Please fill all fields");
            return "posts/create";
        }
        Post newPost = postService.register(postForm.getTitle(),
                                            postForm.getBody());
        User author = userService.findByUsername(postForm.getAuthorName());
        newPost.setAuthor(author);

        newPost.setDate(postForm.getDate());
        postService.create(newPost);
        if ( newPost.getId() > 0) {
            notificationService.addInfoMessage("Post created, Follow next step");
            return "redirect:/posts";
        } else {
            notificationService.addErrorMessage("There is a problem with DB, please try again!");
            return "posts/create";
        }
    }

    @RequestMapping("/posts/comment/{id}")
    public String showPostWithComment(CommentForm commentForm, @PathVariable("id") Long id,  Model model) {
        Post post = postService.findById(id);
        if(post == null) {
            notificationService.addErrorMessage("Cannot find  pos:" + id);
            return "redirect:/";
        }
        List<Comment> comments = commentService.findAllByPostId(id);
        model.addAttribute("comments", comments);
        model.addAttribute("post", post);
        return "posts/viewPostAndCommentAdd";
    }


    @RequestMapping(value = "/posts/comment/{id}", method = RequestMethod.POST)
    public String publishComment(@PathVariable("id") Long id, @Valid CommentForm commentForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            notificationService.addErrorMessage("Please write comment or choose Back link");
            return "redirect:/posts/comment/{id}";
        }
        Comment comment = new Comment();
        comment.setContent(commentForm.getContent());
        Date date = new Date();
        comment.setDate(date);

        Post post = postService.findById(id);
        comment.setPost(post);



        User author = userService.findByUsername(commentForm.getAuthorName());
        comment.setAuthor(author);


        commentService.create(comment);

        notificationService.addInfoMessage("Comment added, Follow next step");
        return "redirect:/share/{id}";

    }
}
