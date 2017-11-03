package example5;

import com.noname.aop.annotation.After;
import com.noname.aop.annotation.Before;
import com.noname.aop.annotation.Proxy;
import com.noname.web.http.HttpHeader;
import com.noname.web.http.Request;
import com.noname.web.http.Response;
import io.netty.handler.codec.http.HttpResponseStatus;

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
