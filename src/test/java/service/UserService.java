package service;

import com.noname.db.Service;
import com.noname.security.User;
import com.noname.security.UserDetailService;

@Service
public class UserService implements UserDetailService {

    @Override
    public User loadUser(String authenticationInfo) {
        return new User("zhuyichen", "12234", new String[]{"STUDENT"});
    }
}
