package org.ink.web;

import org.ink.web.http.HttpSession;
import org.ink.web.http.Request;
import org.ink.web.http.Response;

/**
 * the methods for current context
 *
 * @author zhuyichen
 */
public final class WebContext {


    private static final ThreadLocal<HttpSession> SESSION = new ThreadLocal<>();
    private static final ThreadLocal<Response> RESPONSE = new ThreadLocal<>();
    private static final ThreadLocal<Request> REQUEST = new ThreadLocal<>();

    /**
     * set current session
     * @param session current session context
     */
    public static void setCurrentSession(HttpSession session) {
        SESSION.set(session);
    }

    /**
     * get current session context
     * @return current session context
     */
    public static HttpSession currentSession() {
        return SESSION.get();
    }

    /**
     *  set current response
     */
    public static void setCurrentResponse(Response response) {
        RESPONSE.set(response);
    }

    /**
     * set current request
     */
    public static void setCurrentRequest(Request request) {
        REQUEST.set(request);
    }

    public static Response currentResponse() {
        return RESPONSE.get();
    }

    public static Request currentRequest() {
        return REQUEST.get();
    }
    /**
     * remove current session
     */
    public static void remove() {
        SESSION.remove();
        REQUEST.remove();
        RESPONSE.remove();
    }

}
