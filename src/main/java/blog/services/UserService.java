package blog.services;


import blog.models.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Long id);
    User create(User user);
    User edit(User user);
    void deleteById(Long id);
    boolean authenticate(String username, String password);
    User login(String username, String password);
    User register(String username, String password, String fullName, String email);
    void setPassword(String username, String newPassword);
    String findByUsernameAsString(String username);
    boolean isUserExist(String username);

}
