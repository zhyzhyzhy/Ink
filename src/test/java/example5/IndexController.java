package example5;


import org.ink.web.annotation.Controller;
import org.ink.web.annotation.GET;
import org.ink.web.annotation.PathVariable;

@Controller
public class IndexController {

    @GET("/index/{name}")
    public String getIndex(@PathVariable String name) {
        return name;
    }

}
