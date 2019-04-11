package org.test.di.app;

import org.test.di.factory.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.factory.BeanFactoryFacade;

public class ApplicationContext {
    
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationContext.class);
    
    private BeanFactoryFacade beanFactoryFacade = new BeanFactoryFacade();

    public ApplicationContext(String basePackage) {
        LOG.info("          ==========   Welcome   ==========          ");
        beanFactoryFacade.initiate(basePackage);
    }
    
    public Object getBean(String beanName){
        return null;
    }

    public BeanFactory getBeanFactory(){
        return beanFactoryFacade.getBeanFactory();
    }
    
    public void close() {
        beanFactoryFacade.getBeanFactory().close();
    }
}