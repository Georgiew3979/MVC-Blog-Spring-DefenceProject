package blog.controllers;

import blog.models.Comment;
import blog.models.Post;
import blog.services.CommentService;
import blog.services.NotificationService;
import blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping("/")
    public String home(Model model) {
        List<Post> latest5Posts = postService.findLatest5();
        model.addAttribute("latest5posts", latest5Posts);
        List<Post> latest3Posts = latest5Posts.stream()
                .limit(3).collect(Collectors.toList());
        model.addAttribute("latest3posts", latest3Posts);
        return "index";

    }

    @RequestMapping("/posts/{id}")
    public String view(@PathVariable("id") Long id, Model model) {

        List<Post> latest5Posts = postService.findLatest5();
        model.addAttribute("latest5posts", latest5Posts);

        Post post = postService.findById(id);

        if(post == null) {
            notificationService.addErrorMessage("Cannot find post" + id);
            return "redirect:/";
        }
        List<Comment> comments = commentService.findAllByPostId(id);
        model.addAttribute("comments", comments);
        model.addAttribute("post", post);
        return "viewPost";
    }

}
