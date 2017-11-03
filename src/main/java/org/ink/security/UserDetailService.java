package org.ink.security;


public interface UserDetailService {
    //get roles from authenticationInfo( name or id )
    User loadUser(String authenticationInfo);
}
