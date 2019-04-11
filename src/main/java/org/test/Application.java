package org.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.bean.Printer;
import org.test.di.app.ApplicationContext;
import org.test.di.factory.BeanFactory;

public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContext("org.test.bean");
        BeanFactory beanFactory = applicationContext.getBeanFactory();
        String[] strings = beanFactory.getAllBeans().toArray(new String[0]);
        LOG.info("Get all available beans <{}>", (Object) strings);
        LOG.info("Get bean from context");
        Printer printer = (Printer) beanFactory.getBean("printer");
        printer.print("Hello World!");
        applicationContext.close();
    }

}
