package example1;

import org.ink.Ink;
import org.ink.web.annotation.*;
import org.ink.web.http.HttpHeader;
import org.ink.web.http.Response;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class HelloController {

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
        return Response
                .status(HttpResponseStatus.OK)
                .body(Collections.singletonMap(name, password))
                .header(HttpHeader.CONTENT_TYPE, "application/json")
                .build();

    }

    @POST("/new")
    public Response newPerson(@RequestJson User user) {
        System.out.println(user);
        return Response.status(HttpResponseStatus.OK).build();
    }

    @POST("/postt")
    public Response post(@RequestParam String name, @RequestParam String password) {
        System.out.println(name);
        System.out.println(password);
        return Response.ok().build();
    }
    @GET("/user/{id}")
    public Response getId(@PathVariable Long id) {
        return Response.status(HttpResponseStatus.OK).body(id).build();
    }

    @GET("/text")
    public String getText() {
        return "hello,world";
    }

    @GET("/file")
    public Response uploadFile() {
        return new Response.Builder(HttpResponseStatus.OK)
                .header(HttpHeader.CONTENT_TYPE,
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .file(new File("/Users/zhuyichen/cp.sh"), "hahahahahha.sh")
                .build();
    }

    public static void main(String[] args) {


         new Ink(8091, HelloController.class).start();
    }
}
