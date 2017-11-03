package example5;

import org.ink.aop.annotation.After;
import org.ink.aop.annotation.Before;
import org.ink.aop.annotation.Proxy;

@Proxy
public class IndexAop {

    @Before(value = "/index/.*", args = "name")
    public boolean beforeIndex(String name) {
        System.out.printf("before...");
        System.out.println("name = " + name);
        return true;
    }

    @After("/index/.*")
    public void afterIndex() {
        System.out.printf("after ... ");
    }
}
