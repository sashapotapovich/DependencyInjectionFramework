package org.test;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.bean.Printer;
import org.test.di.app.ApplicationContext;
import org.test.di.factory.BeanFactory;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContext("org.test.bean");
        BeanFactory beanFactory = applicationContext.getBeanFactory();
        log.info("Get ALL beans");
        Set<String> allBeans = beanFactory.getAllBeans();
        allBeans.forEach(log::info);
        log.info("Get bean from context");
        Printer printer = (Printer) beanFactory.getBean("printer");
        log.info("Bean - " + printer.getClass().toGenericString());
        printer.print("Hello World!");
        applicationContext.close();
    }

}
