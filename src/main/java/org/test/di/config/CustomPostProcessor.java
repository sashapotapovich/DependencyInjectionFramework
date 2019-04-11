package org.test.di.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomPostProcessor implements BeanPostProcessor {
    
    private static final Logger log = LoggerFactory.getLogger(CustomPostProcessor.class);
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        log.info("---CustomPostProcessor Before " + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        log.info("---CustomPostProcessor After " + beanName);
        return bean;
    }
}