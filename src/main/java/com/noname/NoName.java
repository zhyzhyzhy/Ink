package com.noname;

import com.noname.server.NoNameServer;

/**
 * Created by zhuyichen on 2017/7/11.
 */
public class NoName {
    public static NoNameServer NoName(int port) {
        return new NoNameServer(port);
    }
}
