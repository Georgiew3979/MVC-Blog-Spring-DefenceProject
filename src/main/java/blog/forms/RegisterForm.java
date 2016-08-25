package blog.forms;


import javax.validation.constraints.Size;

public class RegisterForm {
    @Size(min = 2, max = 30, message = "Please insert usr btn 2 i 30")
    private String username;

    private String fullName;

    @Size(min = 2, max = 30, message = "Please insert usr btn 2 i 30")
    private String email;

    @Size(min = 3)
    private String password;

    @Size(min = 3, max = 30 )
    private String passwordConfirm;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
