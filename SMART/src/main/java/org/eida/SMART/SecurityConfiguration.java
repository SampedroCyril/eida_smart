package org.eida.SMART;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/sadmin/**").hasRole("SADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/client/**").hasRole("CLIENT")
                .antMatchers("/").permitAll()
                .and().formLogin().loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(AuthenticationSuccess())
                .permitAll();
    }

    @Bean
    public AuthenticationSuccessHandler AuthenticationSuccess() {
        return new AuthenticationHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new AccessDenialHandler();

    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
