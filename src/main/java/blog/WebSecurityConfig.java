package blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDS;

    //@Autowired
   // private BCryptPasswordEncoder bcryptEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception{

        http
                .authorizeRequests()
                .antMatchers("/","/home","/css/**","/img/**","/js/**","/users/register", "/share/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/users/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .userDetailsService(userDS).passwordEncoder(passwordEncoder());

    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDS;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

}
