package org.test.di.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanFactoryFacade {

    private static final Logger LOG = LoggerFactory.getLogger(BeanFactoryFacade.class);

    private BeanFactory beanFactory = new BeanFactory();


    public void initiate(String basePackage) {
        LOG.info("Context is under construction");
        beanFactory.instantiate(basePackage);
        LOG.info("Bean Post Processors before initialization Beans");
        beanFactory.processBeforeBeanInitialization();
        LOG.info("Initializing Beans");
        beanFactory.populateProperties();
        LOG.info("Bean Post Processors after initialization Beans");
        beanFactory.processAfterBeanInitialization();
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
