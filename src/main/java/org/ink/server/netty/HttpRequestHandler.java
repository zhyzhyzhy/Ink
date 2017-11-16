package org.ink.server.netty;

import org.ink.exception.UnauthorizedException;
import org.ink.web.WebContext;
import org.ink.web.http.HttpResponseBuilder;
import org.ink.web.http.Request;
import org.ink.web.http.Response;
import org.ink.web.route.Route;
import org.ink.web.route.RouteFinder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.ink.web.route.RouteSetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zhuyichen  2017/7/11.
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

        /**
         * build the request
         * if is the first request then create the session
         */
        Request request = new Request(channelHandlerContext.channel(), fullHttpRequest);


        /**
         *
         * TODO
         *
         * 怎么传递set-cookie头。。。。。。。。。。。
         * 。。。。。。。。。。。。。。。
         * 。。。。。。。。。。。。。。。。。
         * 。。。。。。。。。。。。。。。。。
         * 。。。。。。。。。。。。。。。。。。
         *
         *
         *
         */
        //set current session
        WebContext.setCurrentSession(request.getSession());

        Response preparedResponse = new Response(channelHandlerContext.channel(), request);

        Route route = null;
        try {
            route = RouteFinder.findRoute(fullHttpRequest);
            if (route == null) {
                HttpResponse exceptionResponse = HttpResponseBuilder.build(HttpResponseStatus.NOT_FOUND);
                channelHandlerContext.write(exceptionResponse);
                return;
            }
            RouteSetter.routeSetter(route, fullHttpRequest);
        } catch (UnauthorizedException ignored) {
            HttpResponse exceptionResponse = HttpResponseBuilder.build(HttpResponseStatus.UNAUTHORIZED);
            channelHandlerContext.write(exceptionResponse);
            return;
        }

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


        FullHttpResponse response = HttpResponseBuilder.build(channelHandlerContext.channel(), preparedResponse);
        channelHandlerContext.write(response);

    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

}
