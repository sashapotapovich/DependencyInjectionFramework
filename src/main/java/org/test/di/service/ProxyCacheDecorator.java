package org.test.di.service;

import java.lang.reflect.Proxy;
import net.sf.cglib.proxy.Enhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyCacheDecorator {

    private static final Logger log = LoggerFactory.getLogger(ProxyCacheDecorator.class);
    
    private final Cache cache;

    public ProxyCacheDecorator(Cache cache) {
        this.cache = cache;
    }

    public Object getBean(String beanName) {
        Object bean = cache.getBean(beanName);
        log.info(bean.getClass().toGenericString());
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(bean.getClass());
        //Proxy.newProxyInstance(bean.getClass().getClassLoader(), bean.getClass().getInterfaces())
        return null;
    }

    public void addBean(String beanName, Object bean) {
        // add to the list
    }

}
