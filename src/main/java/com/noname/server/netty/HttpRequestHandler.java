package com.noname.server.netty;

import com.alibaba.fastjson.JSON;
import com.noname.web.http.Response;
import com.noname.web.route.Route;
import com.noname.web.route.RouteFinder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by zhuyichen on 2017/7/11.
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final Logger log = LoggerFactory.getLogger(HttpRequestHandler.class);

    public HttpRequestHandler() {
        log.info("init handler...");
    }



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }


    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        if (fullHttpRequest == null) {
            return;
        }

        log.info("Request [{}]", fullHttpRequest.uri());

        Response message = null;

        Route route = RouteFinder.findRoute(fullHttpRequest);

        if (route != null) {
            message = (Response)route.getMethod().invoke(route.getObject(), route.getParamters());
            log.info("Response {{}}", message.getResponseEntity());
        }

        if (message == null) {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.NOT_FOUND, Unpooled.copiedBuffer(page404(fullHttpRequest.uri()).getBytes()));
            channelHandlerContext.write(response);
        } else {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK, Unpooled.wrappedBuffer(JSON.toJSONString(message.getResponseEntity()).getBytes()));
            response.headers().set("Content-Type", "application/json");
            channelHandlerContext.write(response);
        }

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
