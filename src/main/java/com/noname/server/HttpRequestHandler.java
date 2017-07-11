package com.noname.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONWriter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by zhuyichen on 2017/7/11.
 */
public class HttpRequestHandler extends ChannelInboundHandlerAdapter {

    private final Logger log = LoggerFactory.getLogger(HttpRequestHandler.class);
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("connect from {} ", ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            String message = JSON.toJSONString(Collections.singletonMap("message", "hello,world"));

            log.info("response message {}", message);

            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK, Unpooled.wrappedBuffer(message.getBytes()));
            response.headers().set("Content-Type","application/json");
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
