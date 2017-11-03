package example2.web;

import org.ink.security.annotation.Role;
import org.ink.web.annotation.Controller;
import org.ink.web.annotation.GET;
import example1.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuyichen on 2017/7/12.
 */
@Controller
public class HelloController {

    @Role("ADMIN")
    @GET("/persons")
    public List<User> getUsers() {
        return new ArrayList<User>() {{
            add(new User("zhuyichen", "hello", "cdscds"));
            add(new User("maoshumin", "hello", "cdscds"));
        }};
    }

    @Role("STUDENT")
    @GET("/index")
    public Map<String, String> getIndex() {
        return Collections.singletonMap("hello", "world");
    }

    @GET("/test")
    public Map<String, String> getTest() {
        return Collections.singletonMap("hello","test");
    }
}
