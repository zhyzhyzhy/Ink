package org.ink.security.user;


import org.ink.security.user.User;

public interface UserDetailService {
    //get roles from authenticationInfo( name or id )
    User loadUser(String authenticationInfo);
}
