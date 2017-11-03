package example5;


import com.noname.web.annotation.Controller;
import com.noname.web.annotation.GET;
import com.noname.web.annotation.PathVariable;

@Controller
public class IndexController {

    @GET("/index/{name}")
    public String getIndex(@PathVariable String name) {
        return name;
    }

}
