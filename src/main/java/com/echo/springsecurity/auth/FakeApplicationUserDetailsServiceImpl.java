package com.echo.springsecurity.auth;

import com.google.common.collect.Lists;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.echo.springsecurity.security.ApplicationUserRole.*;

@Repository("fakeRepository")
public class FakeApplicationUserDetailsServiceImpl implements ApplicationUserDao{
    private final PasswordEncoder passwordEncoder;

    public FakeApplicationUserDetailsServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByName(String userName) {
        return getApplicationUsers().stream()
                .filter(user -> userName.equals(user.getUsername()))
                .findFirst();
    }

    // fake data source to get all useers
    private List<ApplicationUser> getApplicationUsers(){
        return Lists.newArrayList(
                new ApplicationUser("student",
                        passwordEncoder.encode("password"),
                        true,
                        true,
                        true,
                        true,
                        STUDENT.getGrantedAuthority()
                ),
                new ApplicationUser("admin",
                        passwordEncoder.encode("password"),
                        true,
                        true,
                        true,
                        true,
                        ADMIN.getGrantedAuthority()
                ),
                new ApplicationUser("adminTrainee",
                        passwordEncoder.encode("password"),
                        true,
                        true,
                        true,
                        true,
                        ADMIN_TRAINEE.getGrantedAuthority()
                )
        );
    }
}
