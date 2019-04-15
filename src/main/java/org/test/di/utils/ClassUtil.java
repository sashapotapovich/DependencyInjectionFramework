package org.test.di.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ClassUtil.class);
    
    public static ClassLoader getClassLoader(){
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
            LOG.info("{} Classloader will be used", "Thread");
        } catch (Throwable ex) {
            LOG.error(ex.toString());
        }
        if (classLoader == null) {
            classLoader = ClassUtil.class.getClassLoader();
            LOG.info("{} Classloader will be used", "Class");
            if (classLoader == null) {
                try {
                    classLoader = ClassLoader.getSystemClassLoader();
                    LOG.info("{} Classloader will be used", "System");
                } catch (Throwable ex) {
                    LOG.error(ex.toString());
                }
            }
        }
        return classLoader;
    }

}
