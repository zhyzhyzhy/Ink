package com.noname.server;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;

/**
 * Created by zhuyichen on 2017/7/11.
 */
public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(
                new HttpServerCodec(),
                new HttpServerExpectContinueHandler(),
                new HttpRequestHandler()
        );
    }
}
