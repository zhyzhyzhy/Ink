package org.ink.example1;


import org.ink.web.annotation.*;
import org.ink.web.http.Response;
import org.ink.web.view.Model;

import java.util.Collections;
import java.util.Map;

@Controller
public class IndexController {


    @GET("/index")
    public String getIndex() {
        return "hello,index";
    }

    @GET("/index/{id}")
    public Map<Integer, String> getIndexId(@PathVariable Integer id) {
        return Collections.singletonMap(id, "Index");
    }

    @POST("/student")
    public Response newStudent(@RequestJson Student student) {
        return Response.ok().build();
    }

    @View
    @GET("/view")
    public String getView(Model model) {
        model.attr("user", "zhuyichen");
        return "index.ftl";
    }
}
