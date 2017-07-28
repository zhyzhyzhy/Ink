package com.noname.security;


public interface UserDetailService {
    //get roles from authenticationInfo( name or id )
    User loadUser(String authenticationInfo);
}
