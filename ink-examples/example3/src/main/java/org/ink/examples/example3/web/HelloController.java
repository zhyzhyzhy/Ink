package org.ink.examples.example3.web;

import org.ink.examples.example3.domain.Student;
import org.ink.security.annotation.Role;
import org.ink.web.annotation.Controller;
import org.ink.web.annotation.GET;

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
    public List<Student> getUsers() {
        ArrayList<Student> students =  new ArrayList<Student>();
        students.add(new Student("zhuyichen", "hello"));
        students.add(new Student("wangshukang", "hello"));
        return students;
    }

    @GET("/index")
    public Map<String, String> getIndex() {
        return Collections.singletonMap("hello", "world");
    }

    @GET("/test")
    public Map<String, String> getTest() {
        return Collections.singletonMap("hello","test");
    }
}
