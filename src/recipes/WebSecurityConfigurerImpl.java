package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfigurerImpl extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(getEncoder());

/*    //for development only
        auth
                .inMemoryAuthentication() // user store 2
                .withUser("user").password("123").roles("ADMIN")
                .and().passwordEncoder(NoOpPasswordEncoder.getInstance());*/
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/api/register").permitAll()
                .mvcMatchers("/h2-console").hasRole("ADMIN")
                .mvcMatchers("/actuator/shutdown").permitAll()
                .anyRequest().authenticated() // make remaining endpoints public (including POST /register)
                .and()
                .csrf().disable()// disabling CSRF will allow sending POST request using Postman
                .headers().frameOptions().disable()// disabling this to use H2 console
                .and()
                .formLogin()
                .and()
                .httpBasic(); // enables basic auth.
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}