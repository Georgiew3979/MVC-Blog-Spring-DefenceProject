package blog.controllers;

import blog.forms.LoginForm;
import blog.services.NotificationService;
import blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class AccountController {

    @Autowired
    public NotificationService notificationService;

    @Autowired
    private UserService userService;

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

        //if (userService.authenticate(loginForm.getUsername(),loginForm.getPassword())) {
        //   notificationService.addInfoMessage("Follow next step");
        //    return  "redirect:/";
        //}
        if (userService.login(loginForm.getUsername(), loginForm.getPassword()) != null) {
            notificationService.addInfoMessage("Follow next step");
            return "redirect:/";
        } else {
            notificationService.addErrorMessage("Username / password is wrong");
            return "users/login";
        }


    }

}
