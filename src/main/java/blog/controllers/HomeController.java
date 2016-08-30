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

import javax.servlet.http.HttpSession;
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
        model.addAttribute("page", 0);
        return "index";

    }

    // @RequestMapping("/share/viewAllPosts")
    // public String viewAll(Model model) {
    //    List<Post> allPosts = postService.findAll();
    //    model.addAttribute("allposts", allPosts);
    //   model.addAttribute("page", 0);
    //   return "/share/viewAllPosts";
    //}

    @RequestMapping("/share/viewNext/{page}")
    public String viewNext(@PathVariable("page") Integer page,Model model) {
       List<Post> allPosts = postService.findAll();
       if( allPosts.size()/5 > page) {
           page += 1;
       } else { page = 0;}

        List<Post> nextPosts = postService.findNext5(page,5);
        for (Post post: nextPosts) {
            post.setBody(cutLongTextDouble(post.getBody()));
        }
        model.addAttribute("nextposts", nextPosts);

        model.addAttribute("page", page);
        return "/share/viewAllPosts";

    }

    private String cutLongTextDouble(String text)
    {
        int maxSize = 400;
        String cutedText = text.substring(0, Math.min(text.length(),maxSize));
        if (text.length() > maxSize) {
            text = cutedText + "  ...";
        }
        return text;
    }


    @RequestMapping("/share/{id}")
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
        model.addAttribute("page", 1);
        return "/share/viewPost";
    }

}
