package com.echo.springsecurity.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.echo.springsecurity.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE)), // using static import
    ADMIN_TRAINEE(Sets.newHashSet(COURSE_READ, STUDENT_READ)),
    STUDENT (Sets.newHashSet());

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    // SimpleGranted authority stores a string representation of the authority (role)
    public Set<SimpleGrantedAuthority> getGrantedAuthority(){
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(element -> new SimpleGrantedAuthority(element.getPermission()))        // making each element of the permissions an SimpleGrantedAuthority
                .collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority("ROLE_"+this.name()));               // adding the actual role as well, final object contains Role and permission
        return permissions;
    }
}
