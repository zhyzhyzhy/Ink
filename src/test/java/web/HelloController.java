package web;

import com.alibaba.fastjson.JSON;
import com.noname.ioc.annotation.Bean;
import com.noname.web.annotation.Controller;
import com.noname.web.annotation.GET;
import com.noname.web.annotation.PathVariable;
import com.noname.web.annotation.RequestParam;
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
        return new Response(new ArrayList<User>(){{
            add(new User("zhuyichen","hello","cdscds"));
            add(new User("maoshumin","hello","cdscds"));
        }}, HttpResponseStatus.OK);
    }
   @GET("/index")
   public Response getIndex(@RequestParam String name,
                            @RequestParam String password) {
        return new Response(name + " " + password, HttpResponseStatus.OK);
   }

}
