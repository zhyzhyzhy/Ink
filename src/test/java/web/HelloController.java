package web;

import com.alibaba.fastjson.JSON;
import com.noname.ioc.annotation.Bean;
import com.noname.web.annotation.Controller;
import com.noname.web.annotation.GET;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuyichen on 2017/7/12.
 */
@Controller
public class HelloController {
    @GET("/love")
    public String getIndex() {
        return "hello";
    }
    @GET("/index")
    public Map<String, String> getLove() {
        return Collections.singletonMap("message", "love");
    }
    @GET("/int")
    public int getInt() {
        return 2;
    }
    @GET("/person")
    public User getUser1() {
        return new User("maoshumin","hello","cdscds");
    }
    @GET("/person")
    public User getUser() {
        return new User("zhuyichen","hello","cdscds");
    }
    @GET("/persons")
    public List<User> getUsers() {
        return new ArrayList<User>(){{
            add(new User("zhuyichen","hello","cdscds"));
            add(new User("maoshumin","hello","cdscds"));
        }};
    }
}
