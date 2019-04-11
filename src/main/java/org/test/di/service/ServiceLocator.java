package org.test.di.service;

import javax.naming.InitialContext;

public class ServiceLocator {
    private static final Cache cache = new Cache();
    private static final ProxyCacheDecorator proxyCacheDecorator = new ProxyCacheDecorator(cache);

    public static Object getBean(String beanName) {

        Object bean = cache.getBean(beanName);

        if (bean != null) {
            return bean;
        }

        Object beanProxy = proxyCacheDecorator.getBean(beanName);
        //cache.addBean(beanName, beanProxy);
        return beanProxy;
    }
}
