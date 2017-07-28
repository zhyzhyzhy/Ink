package example2.service;

import com.noname.db.Service;
import com.noname.security.User;
import com.noname.security.UserDetailService;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class UserService implements UserDetailService {

    @Override
    public User loadUser(String authenticationInfo) {

        //in this example authenticationInfo is studentId
        //the password is necessary
        // here i just fake a user with role "STUDENT"
        return new User("zhuyichen", "12234", Arrays.asList("STUDENT"));

        //if not found return null
    }
}