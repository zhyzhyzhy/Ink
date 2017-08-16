package com.noname.server.netty;

import com.alibaba.fastjson.JSON;
import com.noname.exception.UnauthorizedException;
import com.noname.filter.FilterUtil;
import com.noname.web.http.HttpHeader;
import com.noname.web.http.Request;
import com.noname.web.http.Response;
import com.noname.web.route.Route;
import com.noname.web.route.RouteFinder;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhuyichen on 2017/7/11.
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final Logger log = LoggerFactory.getLogger(HttpRequestHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel active");
    }


    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        if (fullHttpRequest == null) {
            return;
        }
        log.info("Request [{}]", fullHttpRequest.uri());



        Request request = new Request(fullHttpRequest);

        Response preparedResponse = new Response(fullHttpRequest);

        if (!FilterUtil.doFilter(request, preparedResponse)) {
            channelHandlerContext.write(
                    processResponse(channelHandlerContext.channel(), preparedResponse)
            );
            return;
        }


        Route route = null;
        try {
             route = RouteFinder.findRoute(fullHttpRequest);
        } catch (UnauthorizedException ignored) {
            HttpResponse exceptionResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED);
            channelHandlerContext.write(exceptionResponse);
            return;
        }

        if (route != null) {
            Object o = route.getMethod().invoke(route.getObject(), route.getParamters());
            if (o instanceof Response) {
                preparedResponse = Response.mergeResponse(preparedResponse, (Response)o);
            }
            else {
                preparedResponse.setResponseEntity(o);
                preparedResponse.setResponseStatus(HttpResponseStatus.OK);
            }
            log.info("Response {{}}", preparedResponse.getResponseEntity());
        }

        if (route == null) {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
            response.headers().add(HttpHeader.ContentLength.toString(), "0");
            channelHandlerContext.write(response);
        } else {
            FullHttpResponse response = processResponse(channelHandlerContext.channel(), preparedResponse);
            channelHandlerContext.write(response);
        }

    }

    public FullHttpResponse processResponse(Channel channel, Response response) {

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

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("close ctx");
        ctx.close();
    }


}
