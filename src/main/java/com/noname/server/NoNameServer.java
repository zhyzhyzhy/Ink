package com.noname.server;

import com.noname.server.netty.HttpChannelInitializer;
import com.noname.web.route.Route;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by zhuyichen on 2017/7/11.
 */
public class NoNameServer {
    private final Logger log = LoggerFactory.getLogger(NoNameServer.class);
    private int port;
    private List<Route> list;

    public NoNameServer() {
        this.port = 8000;
    }
    public NoNameServer(int port) {
        this.port = port;
    }
    public void start() {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpChannelInitializer(list));
            ChannelFuture future = bootstrap.bind(port).sync();
            log.info("start listen in port {}", port);
            future.channel().closeFuture().sync();
        } catch (Exception e ) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public void setList(List<Route> list) {
        this.list = list;
    }
}
