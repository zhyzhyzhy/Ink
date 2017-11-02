package example5;

import com.noname.aop.annotation.After;
import com.noname.aop.annotation.Before;
import com.noname.aop.annotation.Proxy;

@Proxy
public class IndexAop {

    @Before("/index")
    public void beforeIndex() {
        System.out.println("cccccccccccccc");
    }

    @After("/index")
    public void afterIndex() {
        System.out.println("after ..............");
    }
}
