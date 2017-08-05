package com.noname.server.netty;

import com.alibaba.fastjson.JSON;
import com.noname.exception.UnauthorizedException;
import com.noname.filter.FilterUtil;
import com.noname.web.http.Request;
import com.noname.web.http.Response;
import com.noname.web.route.Route;
import com.noname.web.route.RouteFinder;
import io.netty.buffer.Unpooled;
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

    }


    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        if (fullHttpRequest == null) {
            return;
        }

        log.info("Request [{}]", fullHttpRequest.uri());

        Response message = new Response();
        Request request1 = new Request(fullHttpRequest);
        if (!FilterUtil.doFilter(request1, message)) {
            channelHandlerContext.write(
                    processResponse(message)
            );
            return;
        }


        Route route = null;
        try {
             route = RouteFinder.findRoute(fullHttpRequest);
        } catch (UnauthorizedException ignored) {
            HttpResponse request = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.UNAUTHORIZED);
            channelHandlerContext.write(request);
            return;
        }

        if (route != null) {
            Object o = route.getMethod().invoke(route.getObject(), route.getParamters());
            if (o instanceof Response) {
                message = Response.mergeResponse(message, (Response)o);
            }
            else {
                message.setResponseEntity(o);
                message.setResponseStatus(HttpResponseStatus.OK);
            }
            log.info("Response {{}}", message.getResponseEntity());
        }

        if (route == null) {

            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.NOT_FOUND, Unpooled.copiedBuffer(page404(fullHttpRequest.uri()).getBytes()));
            channelHandlerContext.write(response);


        } else {
            FullHttpResponse response = processResponse(message);
            channelHandlerContext.write(response);
        }

    }

    public FullHttpResponse processResponse(Response response) {

        DefaultFullHttpResponse fullHttpResponse = null;

        //work
        if (response.getResponseEntity() == null) {
                fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, response.getResponseStatus());
        }
        else {
            //work
            fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, response.getResponseStatus(), Unpooled.copiedBuffer(JSON.toJSONString(response.getResponseEntity()).getBytes()));
        }

        //work
        for (String s : response.getHeaders().keySet()) {
            fullHttpResponse.headers().add(s, response.getHeaders().get(s));
        }
        //work
        if (!fullHttpResponse.headers().contains("Content_type")) {
            fullHttpResponse.headers().set("Content-Type", "application/json");
        }

        return fullHttpResponse;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    public String page404(String url) {
        return "<h1>URL [ " + url + " ] Not Found</h1>";
    }
}
