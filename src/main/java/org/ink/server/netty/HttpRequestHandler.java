package org.ink.server.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.stream.ChunkedFile;
import org.ink.security.exception.ForbiddenException;
import org.ink.security.exception.UnauthorizedException;
import org.ink.web.WebContext;
import org.ink.web.http.HttpHeader;
import org.ink.web.http.Request;
import org.ink.web.http.Response;
import org.ink.web.route.Route;
import org.ink.web.route.RouteFinder;
import org.ink.web.route.RouteSetter;
import org.ink.web.view.FreeMarkerResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * the handling process of the request
 *
 * @author zhuyichen  2017/7/11.
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final Logger log = LoggerFactory.getLogger(HttpRequestHandler.class);

    private FreeMarkerResolver resolver = new FreeMarkerResolver();

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
        WebContext.setCurrentRequest(request);


        Response preparedResponse = new Response(request);
        WebContext.setCurrentResponse(preparedResponse);


        Route route = null;
        try {
            route = RouteFinder.findRoute(request);
            if (route == null) {
                HttpResponse exceptionResponse = Response.buildDefaultFullHttpResponse(HttpResponseStatus.NOT_FOUND);
                channelHandlerContext.write(exceptionResponse);
                return;
            }
            RouteSetter.routeSetter(route, fullHttpRequest);
        } catch (Exception e) {
            handleException(channelHandlerContext, e);
            return;
        }

        Boolean continueProcess = true;
        //before aop
        if (route.beforeProxyChain().size() != 0) {
            continueProcess = route.beforeProxyChain().doChain(request, preparedResponse, route);
        }

        if (continueProcess) {
            Object o = route.getMethod().invoke(route.getObject(), route.getParamters());
            //after aop
            if (route.afterProxyChain().size() != 0) {
                route.afterProxyChain().doChain(request, preparedResponse, route);
            }

            if (o instanceof Response) {
                preparedResponse = Response.mergeResponse(preparedResponse, (Response) o);
                WebContext.setCurrentResponse(preparedResponse);
            }
            else if (route.view()) {
                preparedResponse.setFile(resolver.resolve(o.toString(), null));
                preparedResponse.setResponseStatus(HttpResponseStatus.OK);
                preparedResponse.header(HttpHeader.CONTENT_TYPE, "text/html");
            }
            else  {
                preparedResponse.setBody(o);
                preparedResponse.setResponseStatus(HttpResponseStatus.OK);
            }
            log.info("Response {{}}", preparedResponse.body());
        }

        HttpResponse response = Response.buildDefaultFullHttpResponse0();
        channelHandlerContext.write(response);
        if (WebContext.currentResponse().file() != null) {
            channelHandlerContext.write(new HttpChunkedInput(new ChunkedFile(WebContext.currentResponse().file())));
        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        WebContext.remove();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.writeAndFlush(Response.buildDefaultFullHttpResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR));
        cause.printStackTrace();
        WebContext.remove();
    }

    private void handleException(ChannelHandlerContext ctx, Exception e) {
        if (e instanceof UnauthorizedException) {
            ctx.write(Response.buildDefaultFullHttpResponse(HttpResponseStatus.UNAUTHORIZED));
        }
        else if (e instanceof ForbiddenException) {
            ctx.write(Response.buildDefaultFullHttpResponse(HttpResponseStatus.FORBIDDEN));
        }
        else {
            ctx.write(Response.buildDefaultFullHttpResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
