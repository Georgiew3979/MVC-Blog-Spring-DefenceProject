package blog.controllers;

import blog.forms.LoginForm;
import blog.forms.RegisterForm;
import blog.models.Comment;
import blog.models.Post;
import blog.models.User;
import blog.repositories.AuthUserDetailsService;
import blog.repositories.UserRepository;
import blog.services.CommentService;
import blog.services.NotificationService;
import blog.services.PostService;
import blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class AccountController {



    @Autowired
    public NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;
    @Autowired
    private AuthUserDetailsService authUserDetailsService;

    @RequestMapping("/users")
    public String viewAllUsers(Model model) {
        if(!authUserDetailsService.isAuthorize()){
            notificationService.addAlertMessage("There is not allowed to be here");
            return "redirect:/";
        }
        List<User> users = userService.findAll();
        if(users == null) {
            notificationService.addErrorMessage("Cannot find any user");
            return "redirect:/";
        }

        model.addAttribute("users", users);
        return "users/index";
    }

    @RequestMapping("/users/login")
    public String showLoginForm(LoginForm loginForm){
        return "users/login";
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public String showLoginForm(@Valid LoginForm loginForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {

            notificationService.addErrorMessage("Please fill all fields");
            return "users/login";
        }

        if (userService.login(loginForm.getUsername(), loginForm.getPassword()) != null) {
            notificationService.addInfoMessage("Follow next step");
            return "redirect:/";
        } else {
            notificationService.addErrorMessage("Username / password is wrong");
            return "users/login";
        }


    }

    @RequestMapping("/users/register")
    public String showRegisterForm(RegisterForm registerForm){
        return "users/register";
    }

    @RequestMapping(value = "/users/register", method = RequestMethod.POST)
    public String showRegisterForm(@Valid RegisterForm registerForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {

            notificationService.addErrorMessage("Please fill all fields");
            return "users/register";
        }

        String currentUsername = registerForm.getUsername();

        if(userService.isUserExist(currentUsername)) {
            notificationService.addErrorMessage("This username was rezeved.Please choose another username ");
            return "users/register";
        }


        if(!registerForm.getPassword().equals(registerForm.getPasswordConfirm())){
            notificationService.addErrorMessage("The password and confirm password are not equal ");
            return "users/register";
        }
        //// TODO: 24/08/2016  validation email
        User newUser = userService.register(registerForm.getUsername(),
                                                registerForm.getPassword(),
                                                registerForm.getFullName(),
                                                registerForm.getEmail());
        //TODO set Roles
        newUser.setRoles(2);
        userService.create(newUser);
        if ( newUser.getId() > 0) {
            notificationService.addInfoMessage("Follow next step");
            return "redirect:/users/login";
        } else {
            notificationService.addErrorMessage("There is a problem with DB, please try again!");
            return "users/register";
        }


    }


    @RequestMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        if(user == null) {
            notificationService.addErrorMessage("Cannot find  user:" + id);
            return "redirect:users/index";
        }

        model.addAttribute("user", user);
        return "users/delete";
    }

    @RequestMapping(value = "/users/delete/{id}", method = RequestMethod.POST)
    public String deleteUserById(@PathVariable("id") Long id) {
        if(!authUserDetailsService.isAuthorize()){
            notificationService.addAlertMessage("There is not allowed to be here");
            return "redirect:/";
        }
        User user = userService.findById(id);
        if(user == null) {
            notificationService.addErrorMessage("Cannot find  user:" + id);
            return "redirect:users/index";
        }
        // delete comment for posts created by user
        List<Comment> comments = commentService.findAllByAuthorId(id);
        if (!comments.isEmpty()) {
            for (Comment comment : comments) {
                commentService.deleteById(comment.getId());
            }
        }

        //delete posts from user
        List<Post> posts = postService.findAll();
        if (!posts.isEmpty()) {
            for (Post post : posts) {
                if(post.getAuthor().getId() == id) {
                    postService.deleteById(post.getId());
                }
            }
        }

        userService.deleteById(id);
        if(userService.findById(id) == null) {
            notificationService.addInfoMessage("The user was deleted");
            return "redirect:/users";
        } else {
            notificationService.addErrorMessage("Cannot delete  user:" + id);
        }
        return "users/delete";
    }

    @RequestMapping("/users/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        if(user == null) {
            notificationService.addErrorMessage("Cannot find  user:" + id);
            return "redirect:users/index";
        }

        model.addAttribute("user", user);
        return "users/edit";
    }

    @RequestMapping(value = "/users/edit/{id}", method = RequestMethod.POST)
    public String edituser(@PathVariable("id") Long id, @Valid RegisterForm registerForm, BindingResult bindingResult) {
        if(!authUserDetailsService.isAuthorize()){
            notificationService.addAlertMessage("There is not allowed to be here");
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notificationService.addErrorMessage("Please fill all fields");
            return "users/edit";
        }
        User user = userService.findById(id);
        user.setUsername(registerForm.getUsername());
        user.setFullName(registerForm.getFullName());
        user.setEmail(registerForm.getEmail());


        userService.edit(user);

        notificationService.addInfoMessage("user editted, Follow next step");
        return "redirect:/users";

    }
}
