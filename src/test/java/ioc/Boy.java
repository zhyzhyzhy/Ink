package ioc;

import com.noname.ioc.annotation.Bean;
import com.noname.ioc.annotation.Inject;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * Created by zhuyichen on 2017/7/11.
 */
@Bean
public class Boy {
    @Inject
    private Girl girl;

    @Inject
    private NioEventLoopGroup nioEventLoopGroup;

    public Boy(){}

    public Girl getGirl() {
        return girl;
    }
    public NioEventLoopGroup getNioEventLoopGroup() {
        return nioEventLoopGroup;
    }

    @Override
    public String toString() {
        return "Boy{" +
                "girl=" + girl +
                ", nioEventLoopGroup=" + nioEventLoopGroup +
                '}';
    }
}