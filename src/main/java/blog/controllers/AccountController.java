package blog.controllers;

import blog.forms.LoginForm;
import blog.services.NotificationService;
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

    @RequestMapping("/users/login")
    public String showLoginForm(LoginForm loginForm){
        return "users/login";
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public String showLoginForm(@Valid LoginForm loginForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            notificationService.addAlertMessage("Atention");
            notificationService.addInfoMessage("Follow next step");
            notificationService.addErrorMessage("Please fill all fields");
            return "users/login";
        }
        return "users/login";
    }

}
