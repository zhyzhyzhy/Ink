package com.noname.filter;

import com.noname.web.http.Request;
import com.noname.web.http.Response;

public interface BeforeFilter {

    //true do the next filter , false stop response
    boolean doFilter(Request request, Response response);

}
