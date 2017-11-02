package com.noname.aop;


import java.util.ArrayList;
import java.util.List;

public class ProxyChain {
    private List<ProxyEntity> proxyEntities = new ArrayList<>();

    public void addProxyEntity(ProxyEntity proxyEntity) {
        proxyEntities.add(proxyEntity);
    }

    public int size() {
        return proxyEntities.size();
    }

    public void doChain(Object[] objects) {
        proxyEntities.forEach(proxyEntity -> proxyEntity.doAction(objects));
    }


}
