package org.ink.aop;


import org.ink.web.http.Request;
import org.ink.web.http.Response;
import org.ink.web.route.Route;

import java.util.ArrayList;
import java.util.List;

public class ProxyChain {

    private List<ProxyEntity> proxyEntities = new ArrayList<>();

    private boolean isBeforeChain = false;

    public ProxyChain(boolean isBeforeChain) {
        this.isBeforeChain = isBeforeChain;
    }

    public void addProxyEntity(ProxyEntity proxyEntity) {
        proxyEntities.add(proxyEntity);
    }

    public int size() {
        return proxyEntities.size();
    }

    public boolean doChain(Request request, Response response, Route route) {
        if (isBeforeChain) {
            for (ProxyEntity entity : proxyEntities) {
                if (!entity.doAction(request, response, route)) {
                    return false;
                }
            }
        }
        else {
            for (ProxyEntity entity : proxyEntities) {
                entity.doAction(request, response, route);
            }
        }
        return true;
    }


}
