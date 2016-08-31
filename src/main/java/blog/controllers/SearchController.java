package blog.controllers;

import blog.models.Comment;
import blog.models.Post;
import blog.models.Post_tags;
import blog.models.Tag;
import blog.services.CommentService;
import blog.services.NotificationService;
import blog.services.PostService;
import blog.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SearchController {


    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private TagService tagService;
    @Autowired
    private NotificationService notificationService;

    @RequestMapping("/share/search/{tag}")
    public String searchByTag(@PathVariable("tag") Long tag,Model model) {
        List<Post_tags> resultByTag = tagService.findAllByTagId(tag);
        List<Post> resultByPost = new ArrayList<Post>();
        for (Post_tags post: resultByTag  ) {
            resultByPost.add(post.getPost());
        }
        if(resultByPost.size()==0){
            notificationService.addAlertMessage("There is no result on search :" + tagService.findById(tag).getName());
            return "redirect:/";
        }
        model.addAttribute("resultByPost", resultByPost);

        List<Tag> tags = tagService.findAll();
        model.addAttribute("tags", tags);
        model.addAttribute("page", 0);
        return "/share/searchResult";

    }



}
