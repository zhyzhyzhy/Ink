package com.noname.web.http.util;

import com.noname.web.http.HttpSession;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhuyichen on 2017/8/15.
 */
public class SessionUtil {

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    //存放每个EventLoop中的Channel和HttpSession的key，value
    private static ThreadLocal<Map<Channel, HttpSession>> sessions = new ThreadLocal<>();

    public static ThreadLocal<Map<Channel, HttpSession>> getSessions() {
        return sessions;
    }

    //create a random sessionId
    public static String createSessionId() {
        //to do
        return String.valueOf(atomicInteger.addAndGet(1));
    }

    //往当前的EventLoop中存放
    public static void addSession(Channel channel) {
        if (SessionUtil.getSessions().get() == null) {
            Map<Channel, HttpSession> map = new HashMap<>();
            SessionUtil.getSessions().set(map);
        }

        Map<Channel, HttpSession> map = SessionUtil.getSessions().get();
        if (map.get(channel) == null) {
            HttpSession httpSession = new HttpSession(channel);
            httpSession.setSessionId(SessionUtil.createSessionId());
            map.put(channel, httpSession);
        }
    }

    //得到当前的channel的session
    public static HttpSession getSession(Channel channel) {
        Map<Channel, HttpSession> map = SessionUtil.getSessions().get();
        return map.get(channel);
    }

    //更新session
    public static void updateSession(HttpSession session) {
        if (SessionUtil.getSessions().get() == null) {
            Map<Channel, HttpSession> map = new HashMap<>();
            SessionUtil.getSessions().set(map);
        }
        Map<Channel, HttpSession> map = SessionUtil.getSessions().get();
        map.put(session.getChannel(), session);
    }


}
