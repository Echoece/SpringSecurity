package com.echo.springsecurity.security;

import com.echo.springsecurity.auth.ApplicationUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.TimeUnit;

import static com.echo.springsecurity.security.ApplicationUserRole.STUDENT;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)             // for method level security
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserDetailsService userDetailsService;

    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()                                                   // -> Disable csrf
                .authorizeRequests()                                                // -> Authorize requests
                .antMatchers("/","index","/css/*","/js/*").permitAll()   // -> Root, anything that has index, any file on css or js  directory
                .antMatchers("/api/**").hasRole(STUDENT.name())          // -> Any route matching /api/ must have role Student.
                /*
                // this part is commented as we used method level security
                // we define which authority can do what here, both trainee and admin can read the data, but only admin can write
                .antMatchers(HttpMethod.PUT ,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                .antMatchers(HttpMethod.POST ,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE ,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                .antMatchers(HttpMethod.GET ,"/management/api/**").hasAnyRole(ADMIN.name(),ADMIN_TRAINEE.name())
                 */
                .anyRequest()                                                       // -> Any requests
                .authenticated()                                                    // -> Must be authenticated
                .and()
                    //.httpBasic();                                                     // -> Using basic authentication.
                .formLogin()
                    .loginPage("/login").permitAll()                                    // -> form based authentication
                    .defaultSuccessUrl("/courses",true)          // -> default successURL for successful authentication
                    .usernameParameter("username")                                      // customizing user name and password client side name parameter
                    .passwordParameter("password")
                .and()
                .rememberMe()
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))    // defining session remember me session ID validity
                    .key("something very secured")                                      // defining the hashing key for the hashing algorithm
                    .rememberMeParameter("remember-me")
                .and()
                .logout()                                                            // logout, with clear authentication and session, also deleting cookie
                    .logoutUrl("/logout")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID","remember-me")
                    .logoutSuccessUrl("/login");
    }

    /*// This user details service is how we retrieve our users from database. It is an interface which is implemented by several
    // classes. we will use the in in memory userdetails manager for now.
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails admin = User.builder().username("admin")
                .password(passwordEncoder.encode("password"))
                //.roles(ADMIN.name())            // Internally will be ROLE_ADMIN as the prefix is added by default, using static import for the enum
                .authorities(ADMIN.getGrantedAuthority())
                .build();
        UserDetails student = User.builder()
                .username("student")
                .password(passwordEncoder.encode("password"))
                //.roles(STUDENT.name())
                // ROLE_STUDENT + the permissions will be the parameter here. Here we adding both role and permission as simpleGrantedAuthority Object
                .authorities(STUDENT.getGrantedAuthority())
                .build();

        UserDetails adminTrainee = User.builder().username("adminTrainee")
                .password(passwordEncoder.encode("password"))
                //.roles(ADMIN_TRAINEE.name())
                .authorities(ADMIN_TRAINEE.getGrantedAuthority())
                .build();

        return  new InMemoryUserDetailsManager(student, admin, adminTrainee);
    }*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }
}
