package com.noname;

import com.noname.ioc.context.IocContext;
import com.noname.security.SecurityManager;
import com.noname.server.NoNameServer;
import com.noname.web.route.Route;
import com.noname.web.route.RouteRegister;

import java.util.List;

/**
 * Created by zhuyichen on 2017/7/11.
 */
public class NoName {
    private NoNameServer noNameServer;
    private IocContext iocContext;
    private List<Route> routes;

    public NoName(int port, Class<?> configure) {
        noNameServer = new NoNameServer(port);
        iocContext = new IocContext(configure);

        //get all route info
        routes = RouteRegister.registerRoute(iocContext.getDefinitions());
        //get all Service info
        SecurityManager.getRoleServices(iocContext.getDefinitions());

        noNameServer.setList(routes);
    }
    public void start() {
        noNameServer.start();
    }
}
