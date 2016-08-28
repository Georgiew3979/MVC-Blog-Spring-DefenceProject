package blog.repositories;

import blog.models.User;
import blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AuthUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService users;
    private org.springframework.security.core.userdetails.User userdetails;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        // TODO Auto-generated method stub

        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        User user = getUserDetail(username);


        userdetails = new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPasswordHash(),
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                getAuthorities(user.getRoles())
        );

        return userdetails;
    }
    public List<GrantedAuthority> getAuthorities(Integer role) {

        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        if (role.intValue() == 1) {
            authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (role.intValue() == 2) {
            authList.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else if (role.intValue() == 0) {
            authList.add(new SimpleGrantedAuthority("ROLE_GUEST"));
        }
            return authList;

    }

    private User getUserDetail(String username){

        User user = users.findByUsername(username);


        return user;
    }
}
