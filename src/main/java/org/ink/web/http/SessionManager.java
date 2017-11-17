package org.ink.web.http;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @author zhuyichen 2017-8-15
 */
public final class SessionManager {

    //sessionid -> httpSession
    private static Map<String, HttpSession> sessions = new ConcurrentHashMap<>();

    private static Map<String, HttpSession> sessions() {
        return sessions;
    }

    /**
     * get a random sessionId
     */
    static String createSessionId() {
        return HttpKit.createUniqueId();
    }

    static void addSession(String sessionId, Channel channel) {
        Map<String, HttpSession> map = sessions();
        if (map.get(sessionId) == null) {
            HttpSession httpSession = new HttpSession();
            httpSession.setSessionId(sessionId);
            httpSession.setChannel(channel);
            map.put(sessionId, httpSession);
        }
    }

    /**
     * get target httpSession by sessionId
     */
    static HttpSession getSession(String sessionId) {
        Map<String, HttpSession> map = sessions();
        return map.get(sessionId);
    }

    static boolean containsSession(String sessionId) {
        return sessions.containsKey(sessionId);
    }

//    //清除过期session
//    public static void sessionClear() {
//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//        Runnable action = () -> {
//            sessions.forEach((key, cookie) -> {
//                if (cookie.getExpiredTime() < System.currentTimeMillis()) {
//                    sessions.remove(key);
//                }
//            });
//        };
//        System.out.println("start ... ");
//        executor.scheduleAtFixedRate(action, 5, 1, TimeUnit.HOURS);
//    }


    /**
     * remove target session by sessionid
     */
    public static void remove(String sessionid) {
        sessions.remove(sessionid);
    }

}
