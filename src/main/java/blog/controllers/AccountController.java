package blog.controllers;

import blog.forms.LoginForm;
import blog.forms.RegisterForm;
import blog.models.User;
import blog.repositories.UserRepository;
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
        userService.create(newUser);
        if ( newUser.getId() > 0) {
            notificationService.addInfoMessage("Follow next step");
            return "redirect:/users/login";
        } else {
            notificationService.addErrorMessage("There is a problem with DB, please try again!");
            return "users/register";
        }


    }
}
