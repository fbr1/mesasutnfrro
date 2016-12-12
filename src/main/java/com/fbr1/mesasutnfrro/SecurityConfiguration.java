package com.fbr1.mesasutnfrro;


import com.fbr1.mesasutnfrro.model.entity.Account;
import com.fbr1.mesasutnfrro.model.logic.SpringDataJpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SpringDataJpaUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(Account.PASSWORD_ENCODER);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers(HttpMethod.POST, "/rest").hasRole("ADMIN")
                    .antMatchers(HttpMethod.PUT, "/rest/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.PATCH, "/rest/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.DELETE, "/rest/**").hasRole("ADMIN")
                    .and()
                .formLogin()
                    .defaultSuccessUrl("/", true)
                    .loginPage("/login")
                    .and()
                .logout()
                    .logoutSuccessUrl("/");
    }

}
