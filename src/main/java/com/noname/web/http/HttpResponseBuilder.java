package com.noname.web.http;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;

import static com.noname.web.http.HttpHeader.ContentLength;

/**
 * Created by zhuyichen on 2017/8/18.
 */
public class HttpResponseBuilder {

    public static DefaultFullHttpResponse build(HttpResponseStatus status) {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
        response.headers().add(ContentLength.toString(), "0");
        return response;
    }

    public static DefaultFullHttpResponse build(Channel channel, Response response) {
        DefaultFullHttpResponse fullHttpResponse = null;

        //work
        if (response.getResponseEntity() == null) {
            fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, response.getResponseStatus());
        }
        else {
            //work
            fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, response.getResponseStatus(), Unpooled.copiedBuffer(JSON.toJSONString(response.getResponseEntity()).getBytes()));
        }

        //work
        for (String s : response.getHeaders().keySet()) {
            fullHttpResponse.headers().add(s, response.getHeaders().get(s));
        }
        //work
        if (!fullHttpResponse.headers().contains("Content-type")) {
            fullHttpResponse.headers().set("Content-Type", "application/json;charset=utf-8");
        }

        //session action
        fullHttpResponse.headers().add("Connection", "keep-alive");
        fullHttpResponse.headers().add("Content-Length", fullHttpResponse.content().array().length);

        return fullHttpResponse;
    }
}
