package org.test.di.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.service.Cache;

public class SingletonBeanGenerationStrategy implements BeanGenerationStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(SingletonBeanGenerationStrategy.class);

    @Override
    public Object getBean(String beanName) {
        LOG.info("Trying to get Bean as Singleton");
        return Cache.getInstance().getBean(beanName);
    }
}
