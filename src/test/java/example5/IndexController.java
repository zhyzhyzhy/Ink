package example5;


import com.noname.web.annotation.Controller;
import com.noname.web.annotation.GET;

@Controller
public class IndexController {

    @GET("/index")
    public String getIndex() {
        return "hello,world";
    }

}
