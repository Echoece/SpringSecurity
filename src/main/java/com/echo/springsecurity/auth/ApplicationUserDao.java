package com.echo.springsecurity.auth;


import java.util.Optional;

// This is the service/dao object interface for fetching user related data from database, an implementation of this interface will get data from database

public interface ApplicationUserDao {
    Optional<ApplicationUser> selectApplicationUserByName(String userName);
}
