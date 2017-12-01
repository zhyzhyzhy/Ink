package org.ink;

import org.apache.commons.lang3.SystemUtils;
import org.ink.aop.ProxyManager;
import org.ink.db.MybatisConfig;
import org.ink.ioc.IocContext;
import org.ink.security.SecurityManager;
import org.ink.server.InkServer;
import org.ink.web.route.Route;
import org.ink.web.route.RouteRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 * @author zhuyichen
 */
public class Ink {

    private static final Logger log = LoggerFactory.getLogger(Ink.class);

    private InkServer inkServer;
    private IocContext iocContext;
    private List<Route> routes;

    static {
        log.info("OS           : {}", SystemUtils.OS_ARCH);
        log.info("JAVA_HOME    : {}", SystemUtils.JAVA_HOME);
        log.info("JDK_VERSION  : {}", SystemUtils.JAVA_VERSION);
    }
    public Ink(int port, Class<?> configure) {
        inkServer = new InkServer(port);
        iocContext = new IocContext(configure);

        //get all Service info
        SecurityManager.configure(iocContext, configure);

        //get all route info
        routes = RouteRegister.registerRoute(iocContext.getDefinitions());

        //register proxy methods
        ProxyManager.registerProxy(iocContext.getDefinitions(), routes);

        MybatisConfig.configure(iocContext.getBean(configure));

        inkServer.setList(routes);
    }

    /**
     *  start the ink server
     */
    public void start() {
        inkServer.start();
    }

    /**
     * stop the server
     */
    public void stop() {

    }
}
