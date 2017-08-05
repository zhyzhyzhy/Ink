package com.noname.web.http;

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * Created by zhuyichen on 2017/7/12.
 */
public class Request {

    private FullHttpRequest fullHttpRequest;

    public Request(FullHttpRequest fullHttpRequest) {
        this.fullHttpRequest = fullHttpRequest;
    }

    public String getUri() {
        return fullHttpRequest.uri();
    }



}
