package web;

import com.alibaba.fastjson.JSON;
import com.noname.NoName;
import com.noname.ioc.annotation.Bean;
import com.noname.web.annotation.*;
import com.noname.web.http.Response;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuyichen on 2017/7/12.
 */
@Controller
public class HelloController {
    @GET("/persons")
    public Response getUsers() {
        return new Response.Builder().entity(new ArrayList<User>() {{
            add(new User("zhuyichen", "hello", "cdscds"));
            add(new User("maoshumin", "hello", "cdscds"));
        }}).status(HttpResponseStatus.OK).build();
    }

    @GET("/index")
    public Response getIndex(@RequestParam String name,
                             @RequestParam String password) {
        return new Response.Builder().entity(name + " " + password).status(HttpResponseStatus.OK).build();
    }

    @POST("/new")
    public Response newPerson(@RequestJson User user) {
        System.out.println(user);
        return new Response.Builder().build();
    }

    public static void main(String[] args) {
        new NoName(8091, HelloController.class).start();
    }
}
