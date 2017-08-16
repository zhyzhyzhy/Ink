package com.noname.web.http;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhuyichen on 2017/8/15.
 */
public class SessionManager {

    //存放HttpSession的sessionId，value
    private static Map<String, HttpSession> sessions = new ConcurrentHashMap<>();


    private static Map<String, HttpSession> getSessions() {
        return sessions;
    }
    //create a random sessionId
    //一个简单的实现
    public static String createSessionId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    //往当前的EventLoop中存放
    public static void addSession(String sessionId) {
        Map<String, HttpSession> map = getSessions();
        if (map.get(sessionId) == null) {
            HttpSession httpSession = new HttpSession();
            httpSession.setSessionId(createSessionId());
            map.put(httpSession.getSessionId(), httpSession);
        }
    }

    //得到sessionId对应的session
    public static HttpSession getSession(String sessionId) {
        Map<String, HttpSession> map = getSessions();
        return map.get(sessionId);
    }

    //更新session
    public static void updateSession(HttpSession session) {
        Map<String, HttpSession> map = getSessions();
        map.put(session.getSessionId(), session);
    }

}
