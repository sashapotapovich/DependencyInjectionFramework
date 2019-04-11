package org.test.di.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.factory.BeanGenerationStrategy;

public class ServiceLocator {
    
    private static final Logger LOG = LoggerFactory.getLogger(ServiceLocator.class);

    private ServiceLocator() {
    }

    public static Object getBean(String beanName, BeanGenerationStrategy strategy) {
        
        Object bean = strategy.getBean(beanName);

        if (bean != null) {
            return bean;
        }

        InitialContext context = new InitialContext();
        Object beanProxy = context.lookup(beanName);
        LOG.error("Uuuups, Something went wrong");
        return beanProxy;
    }
}
