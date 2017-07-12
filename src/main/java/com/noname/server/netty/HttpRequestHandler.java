package com.noname.server.netty;

import com.alibaba.fastjson.JSON;
import com.noname.web.route.Route;
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

    private List<Route> list;

    public HttpRequestHandler(List<Route> list) {
        this.list = list;
    }

    private final Logger log = LoggerFactory.getLogger(HttpRequestHandler.class);
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("connect from {} ", ctx.channel().remoteAddress());
    }


    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        if (fullHttpRequest != null) {
            Object message = null;
            System.out.println(fullHttpRequest.content().toString(CharsetUtil.UTF_8));
            log.info("request path {}", fullHttpRequest.uri());
            for (Route router : list) {
                if (router.getHttpMethod().equals(fullHttpRequest.method()) && router.getPath().equals(fullHttpRequest.uri())) {
                    message = router.getMethod().invoke(router.getObject());
                }
            }
            log.info("response message {}", message);

            if (message == null) {
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.NOT_FOUND);
                channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            }
            else {
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK, Unpooled.wrappedBuffer(JSON.toJSONString(message).getBytes()));
                response.headers().set("Content-Type","application/json");
                channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            }

        } else {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.NOT_FOUND);
            channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
