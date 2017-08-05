package example3;

import com.noname.filter.BeforeFilter;
import com.noname.filter.Filter;
import com.noname.web.http.HttpHeader;
import com.noname.web.http.Request;
import com.noname.web.http.Response;
import io.netty.handler.codec.http.HttpResponseStatus;

@Filter("/.*")
public class CountFilter implements BeforeFilter{
    @Override
    public boolean doFilter(Request request, Response response) {

        System.out.println("hello");
        response.setResponseStatus(HttpResponseStatus.OK);
        return false;
    }
}
