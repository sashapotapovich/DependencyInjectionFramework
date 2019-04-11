package org.test.di.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ClassUtil.class);
    
    public static ClassLoader getClassLoader(){
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            LOG.error(ex.toString());
        }
        if (classLoader == null) {
            classLoader = ClassUtil.class.getClassLoader();
            if (classLoader == null) {
                try {
                    classLoader = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    LOG.error(ex.toString());
                }
            }
        }
        return classLoader;
    }

}
