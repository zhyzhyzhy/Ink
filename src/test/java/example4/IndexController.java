package example4;

import com.noname.ioc.annotation.Inject;
import com.noname.web.annotation.Controller;
import com.noname.web.annotation.GET;

/**
 * Created by zhuyichen on 2017/8/9.
 */
@Controller
public class IndexController {

    @Inject
    private StudentService studentService;

    @GET("/index")
    public Student getStudent() {
        return studentService.findByStudentId("B15041116");
    }
}
