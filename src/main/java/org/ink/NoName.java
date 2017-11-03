package org.ink;

import org.ink.aop.ProxyManager;
import org.ink.ioc.context.IocContext;
import org.ink.security.SecurityManager;
import org.ink.server.NoNameServer;
import org.ink.web.route.Route;
import org.ink.web.route.RouteRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by zhuyichen on 2017/7/11.
 */
public class NoName {

    private static final Logger log = LoggerFactory.getLogger(NoName.class);

    private NoNameServer noNameServer;
    private IocContext iocContext;
    private List<Route> routes;

    public NoName(int port, Class<?> configure) {
        noNameServer = new NoNameServer(port);
        iocContext = new IocContext(configure);

        //get all Service info
        SecurityManager.configure(iocContext, configure);

        //get all route info
        routes = RouteRegister.registerRoute(iocContext.getDefinitions());

//        FilterUtil.registerFilter(iocContext.getDefinitions());

        ProxyManager.registerProxy(iocContext.getDefinitions(), routes);


        //db configure
//        MybatisConfig.configure(iocContext.getBean(configure));

        //Mapper injection

        noNameServer.setList(routes);
        log.info(WebConfig.getConfig("name"));

    }
    public void start() {
        noNameServer.start();
    }
}
