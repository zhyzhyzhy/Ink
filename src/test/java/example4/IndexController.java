package example4;

import org.ink.ioc.annotation.Inject;
import org.ink.web.annotation.Controller;
import org.ink.web.annotation.GET;

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
