package org.ink.web;

import org.ink.web.http.HttpSession;

/**
 * the methods for current context
 *
 * @author zhuyichen
 */
public final class WebContext {


    private static final ThreadLocal<HttpSession> SESSIONS = new ThreadLocal<>();

    /**
     * set current session
     * @param session current session context
     */
    public static void setCurrentSession(HttpSession session) {
        SESSIONS.set(session);
    }

    /**
     * get current session context
     * @return current session context
     */
    public static HttpSession currentSession() {
        return SESSIONS.get();
    }

    /**
     * remove current session
     */
    public static void remove() {
        SESSIONS.remove();
    }

}
