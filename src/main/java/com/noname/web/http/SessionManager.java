package com.noname.web.http;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhuyichen on 2017/8/15.
 */
public class SessionManager {

    //存放HttpSession的sessionId，value
    private static Map<Channel, HttpSession> sessions = new ConcurrentHashMap<>();


    private static Map<Channel, HttpSession> getSessions() {
        return sessions;
    }


    //create a random sessionId
    //一个简单的实现
    public static String createSessionId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    //往当前的EventLoop中存放
    public static void addSession(Channel channel, String sessionId) {
        Map<Channel, HttpSession> map = getSessions();
        if (map.get(channel) == null) {
            HttpSession httpSession = new HttpSession();
            httpSession.setSessionId(sessionId);
            map.put(channel, httpSession);
        }
    }

    //得到sessionId对应的session
//    public static HttpSession getSession(String sessionId) {
//        Map<String, HttpSession> map = getSessions();
//        return map.get(sessionId);
//    }

    //得到channel对应的session
    public static HttpSession getSession(Channel channel) {
        Map<Channel, HttpSession> map = getSessions();
        return map.get(channel);
    }
//    //更新session
//    public static void updateSession(HttpSession session) {
//        Map<String, HttpSession> map = getSessions();
//        map.put(session.getSessionId(), session);
//    }

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

    public static void remove(Channel channel) {
        sessions.remove(channel);
    }
}
