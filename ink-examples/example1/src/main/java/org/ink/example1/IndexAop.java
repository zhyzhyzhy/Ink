package org.ink.example1;

import org.ink.aop.annotation.After;
import org.ink.aop.annotation.Before;
import org.ink.aop.annotation.Proxy;

@Proxy
public class IndexAop {

    @Before("/index")
    public boolean beforeIndex() {
        System.out.println("before index");
        return true;
    }

    @After("/index")
    public void afterIndex() {
        System.out.println("after index");
    }

    @Before(value = "/index/.*", args = "id")
    public boolean beforeIndexId(Integer id) {
        System.out.println("indexId = " + id);
        return true;
    }

    @Before(value = "/student", args = "student")
    public boolean getStudent(Student student) {
        System.out.println(student);
        return true;
    }
}
