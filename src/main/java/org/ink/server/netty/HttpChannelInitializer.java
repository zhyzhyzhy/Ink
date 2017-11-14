package org.ink.server.netty;


import org.ink.web.route.Route;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.List;

/**
 * @author zhuyichen
 */
public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {
    private List<Route> list;

    public HttpChannelInitializer(List<Route> list) {
        this.list = list;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(
                new HttpServerCodec(),
                new HttpServerExpectContinueHandler(),
                new HttpObjectAggregator(Integer.MAX_VALUE),
                new ChunkedWriteHandler(),
                new HttpRequestHandler()
        );
    }
}
