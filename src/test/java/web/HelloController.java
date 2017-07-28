package web;

import com.noname.NoName;
import com.noname.security.annotation.Role;
import com.noname.web.annotation.*;
import com.noname.web.http.Response;
import domain.User;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;
import java.util.List;

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

    @GET("/index")
    public Response getIndex(@RequestParam String name,
                             @RequestParam String password) {
        return Response.status(HttpResponseStatus.OK).build();
    }

    @POST("/new")
    public Response newPerson(@RequestJson User user) {
        System.out.println(user);
        return Response.status(HttpResponseStatus.OK).build();
    }

    @GET("/{id}")
    public Response getId(@PathVariable Long id) {
        return Response.status(HttpResponseStatus.OK).body(id).build();
    }

    @GET("/time")
    public Response getTime() {
        return Response.status(HttpResponseStatus.OK).build();
    }

    @GET("/test")
    public Response gettest() {
        return Response.badRequest().build();
    }

    public static void main(String[] args) {
        new NoName(8091, HelloController.class).start();
    }
}
