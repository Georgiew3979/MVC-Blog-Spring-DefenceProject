package blog.controllers;

import blog.models.Post;
import blog.services.NotificationService;
import blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PostController {
    @Autowired
    private PostService postService;

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
        model.addAttribute("posts", posts);
        return "posts/index";
    }

    private String cutLongText(String text)
    {
        int maxSize = 200;
        String cutedText = text.substring(0, Math.min(text.length()-1,maxSize));
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
            return "redirect:posts/index";
        }

        model.addAttribute("post", post);
        return "posts/delete";
    }

    @RequestMapping("/posts/edit/{id}")
    public String editPost(@PathVariable("id") Long id, Model model) {
        Post post = postService.findById(id);
        if(post == null) {
            notificationService.addErrorMessage("Cannot find  pos:" + id);
            return "redirect:posts/index";
        }

        model.addAttribute("post", post);
        return "posts/edit";
    }

}
