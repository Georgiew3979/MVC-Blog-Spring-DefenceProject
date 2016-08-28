package blog.services;

import blog.models.User;
import blog.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Service;

import javax.naming.NameNotFoundException;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserServiceJpaImpl implements UserService {
   @Autowired
   private UserRepository userRepository;


    @Autowired
    private HttpSession httpSession;

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return this.userRepository.findOne(id);
    }

    @Override
    public User create(User user) {
        // todo encrypt the password here

        String password = user.getPasswordHash();
        String hashpassword =BCrypt.hashpw(password,BCrypt.gensalt());
        user.setPasswordHash(hashpassword);
        return this.userRepository.save(user);
    }

    @Override
    public User edit(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        this.userRepository.delete(id);
    }

    @Override
    public boolean authenticate(String username, String password) {
        return true;
    }

    @Override
    public User login(String username, String password) {
       // BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
       // String hashedPassword = passwordEncoder.encode(password);

        User user = this.userRepository.findByUsername(username);
        //user != null && password.equals(user.getPasswordHash()
        if(BCrypt.checkpw(password, user.getPasswordHash())) {
            return user;
        }

        return null;
    }

    @Override
    public User register(String username, String password, String fullName, String email) {
        User user = new User(username, password, fullName, email);
        return user;
    }

    @Override
    public void setPassword(String username, String newPassword) {

    }

    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public boolean isUserExist(String username) {
        List<User> allUsers = this.userRepository.findAll();
        for (User user: allUsers) {
            if(user.getUsername().equals(username)){
                return true;
            }

        }

        return false;
    }
}
