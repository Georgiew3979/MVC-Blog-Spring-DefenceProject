package blog.controllers;


import blog.models.Comment;
import blog.models.User;
import blog.repositories.AuthUserDetailsService;
import blog.services.CommentService;
import blog.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    public NotificationService notificationService;

    @Autowired
    private CommentService commentService;
    @Autowired
    private AuthUserDetailsService authUserDetailsService;

    @RequestMapping("/comments")
    public String viewAllUsers(Model model) {
        if(!authUserDetailsService.isAuthorize()){
            notificationService.addAlertMessage("There is not allowed to be here");
            return "redirect:/";
        }
        List<Comment> comments = commentService.findAll();
        model.addAttribute("comments", comments);

        return "/adminViews/commentsView";
    }

    @RequestMapping("/comments/delete/{id}")
    public String deletePostById(@PathVariable("id") Long id) {

        if (authUserDetailsService.isAuthorize() ) {
            commentService.deleteById(id);
            notificationService.addInfoMessage("The post was deleted");
            return "redirect:/comments";
        }else {
            notificationService.addAlertMessage("There is not allowed to be here");
            return "redirect:/";
        }
    }
}
