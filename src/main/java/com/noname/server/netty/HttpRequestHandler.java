package com.noname.server.netty;

import com.noname.exception.UnauthorizedException;
import com.noname.web.http.HttpResponseBuilder;
import com.noname.web.http.Request;
import com.noname.web.http.Response;
import com.noname.web.http.SessionManager;
import com.noname.web.route.Route;
import com.noname.web.route.RouteFinder;
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

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        if (fullHttpRequest == null) {
            return;
        }
        log.info("Request [{}]", fullHttpRequest.uri());

        Request request = new Request(channelHandlerContext.channel(), fullHttpRequest);

        Response preparedResponse = new Response(channelHandlerContext.channel(), fullHttpRequest);

        Route route = null;
        try {
             route = RouteFinder.findRoute(fullHttpRequest);
        } catch (UnauthorizedException ignored) {
            HttpResponse exceptionResponse = HttpResponseBuilder.build(HttpResponseStatus.UNAUTHORIZED);
            channelHandlerContext.write(exceptionResponse);
            return;
        }

        if (route != null) {
            Boolean continueProcess = true;
            //before aop
            if (route.getBeforeProxyChain().size() != 0) {
                continueProcess = route.getBeforeProxyChain().doChain(request, preparedResponse, route);
            }

            if (continueProcess) {
                Object o = route.getMethod().invoke(route.getObject(), route.getParamters());

                //after aop
                if (route.getAfterProxyChain().size() != 0) {
                    route.getAfterProxyChain().doChain(request, preparedResponse, route);
                }

                if (o instanceof Response) {
                    preparedResponse = Response.mergeResponse(preparedResponse, (Response) o);
                } else {
                    preparedResponse.setResponseEntity(o);
                    preparedResponse.setResponseStatus(HttpResponseStatus.OK);
                }
                log.info("Response {{}}", preparedResponse.getResponseEntity());
            }
        }

        if (route == null) {
            HttpResponse response = HttpResponseBuilder.build(HttpResponseStatus.NOT_FOUND);
            channelHandlerContext.write(response);
        } else {
            FullHttpResponse response = HttpResponseBuilder.build(channelHandlerContext.channel(), preparedResponse);
            channelHandlerContext.write(response);
        }

    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.debug("remove session {}", SessionManager.getSession(ctx.channel()).getSessionId());
        SessionManager.remove(ctx.channel());
        ctx.close();
    }
}
