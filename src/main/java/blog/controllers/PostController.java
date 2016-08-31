package blog.controllers;

import blog.forms.CommentForm;
import blog.forms.PostForm;
import blog.models.*;
import blog.repositories.AuthUserDetailsService;
import blog.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;
import java.util.ArrayList;
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
    private TagService tagService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AuthUserDetailsService authUserDetailsService;

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
    public String deletePostById(@PathVariable("id") Long id, @Valid PostForm postForm) {
        Post post = postService.findById(id);
        if(post == null) {
            notificationService.addErrorMessage("Cannot find  pos:" + id);
            return "redirect:/posts";
        }
        //check is user has a right

        if(authUserDetailsService.isAuthorize() || postForm.getAuthorName().equals(post.getAuthor().getUsername()) ){

            // delete comment first
            List<Comment> comments = commentService.findAllByPostId(id);
            if (!comments.isEmpty()) {
                for (Comment comment : comments) {
                    commentService.deleteById(comment.getId());
                }
            }
            //delete post_tags TODO
            List<Post_tags> post_tags = tagService.findAllByPostId(id);
            for (Post_tags tag : post_tags) {
                if(tag.getPost().getId() == id){
                   tagService.deletePost_tagsById(tag.getId());
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

        }else {
            notificationService.addAlertMessage("There is not allowed to be here");
            return "redirect:/";
        }
    }

    @RequestMapping("/posts/edit/{id}")
    public String editPost(@PathVariable("id") Long id, Model model) {
        Post post = postService.findById(id);
        if(post == null) {
            notificationService.addErrorMessage("Cannot find  pos:" + id);
            return "redirect:/posts";
        }
        List<Tag> tags = tagService.findAll();
        model.addAttribute("tags", tags);
        List<Post_tags> post_tagses  = tagService.findAllByPostId(id);
        List<Tag> tags_old = new ArrayList<Tag>();
        for (Post_tags post_tag: post_tagses ) {
            tags_old.add(post_tag.getTag());
        }
        model.addAttribute("tags_old", tags_old);
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

        //check is user has a right
        if( authUserDetailsService.isAuthorize() || postForm.getAuthorName().equals(post.getAuthor().getUsername())){
        post.setTitle(postForm.getTitle());
        post.setBody(postForm.getBody());

        User author = userService.findByUsername(postForm.getAuthorName());
        post.setAuthor(author);

        Date date = new Date();
        post.setDate(date);

        postService.edit(post);

        //delete old post_tags TODO
            List<Post_tags> post_tags = tagService.findAllByPostId(id);
            for (Post_tags tag : post_tags) {
                if(tag.getPost().getId() == id){
                    tagService.deletePost_tagsById(tag.getId());
                }
            }

        //create new tags records
        for (Tag tag : postForm.getTags()) {
            Post_tags post_tagsNew = new Post_tags(tag, post);
            tagService.create(post_tagsNew);
        }



            notificationService.addInfoMessage("Post editted, Follow next step");
        return "redirect:/posts";

        }else {
            notificationService.addAlertMessage("There is not allowed to be here");
            return "redirect:/";
        }

    }

    @RequestMapping("/posts/create")
    public String showPostForm (PostForm postForm, Model model) {
        List<Tag> tags = tagService.findAll();
        model.addAttribute("tags", tags);
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
        //create tags records
        for (Tag tag : postForm.getTags()) {
            Post_tags post_tags = new Post_tags(tag, newPost);
            tagService.create(post_tags);
        }

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
