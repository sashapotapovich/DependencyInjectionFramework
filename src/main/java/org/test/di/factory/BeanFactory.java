package org.test.di.factory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;
import org.test.di.config.BeanPostProcessor;

public class BeanFactory {
    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);
    private Map<String, Object> beans = new HashMap();
    private List<BeanPostProcessor> postProcessors = new ArrayList<>();

    public void addPostProcessor(BeanPostProcessor postProcessor) {
        postProcessors.add(postProcessor);
    }

    public void instantiate(String basePackage) {
        try {
            ClassLoader classLoader = null;
            try {
                classLoader = Thread.currentThread().getContextClassLoader();
            } catch (Throwable ex) {
            }
            if (classLoader == null) {
                classLoader = BeanFactory.class.getClassLoader();
                if (classLoader == null) {
                    try {
                        classLoader = ClassLoader.getSystemClassLoader();
                    } catch (Throwable ex) {
                    }
                }
            }
            String path = basePackage.replace('.', File.separatorChar);
            log.info(path);
            Enumeration<URL> resourceUrls = classLoader.getResources(File.separatorChar + path);
            log.info(resourceUrls.toString());
            while (resourceUrls.hasMoreElements()) {
                URL url = resourceUrls.nextElement();
                File file = new File(url.toURI());
                for (File classFile : file.listFiles()) {
                    String fileName = classFile.getName();
                    String className = null;
                    if (fileName.endsWith(".class")) {
                        className = fileName.substring(0, fileName.lastIndexOf("."));
                    }
                    Class classObject = Class.forName(basePackage + "." + className);
                    if (classObject.isAnnotationPresent(Component.class)) {
                        log.info("======Component: " + classObject);
                        Object instance = classObject.newInstance();
                        String beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
                        beans.put(beanName, instance);
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (InstantiationException e) {
            log.error(e.getMessage());
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        } catch (URISyntaxException e) {
            log.error(e.getMessage());
        }
    }

    public void populateProperties() {
        try {
            beans.values().forEach(value -> log.info(value.toString()));
            for (Object object : beans.values()) {
                for (Field field : object.getClass().getDeclaredFields()) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        for (Object dependency : beans.values()) {
                            if (dependency.getClass().equals(field.getType())) {
                                if (field.isAccessible()) {
                                    field.set(object, dependency);
                                } else {
                                    field.setAccessible(true);
                                    field.set(object, dependency);
                                    field.setAccessible(false);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
        }
    }

    public void initializeBeans() {
        for (String name : beans.keySet()) {
            Object bean = beans.get(name);
            for (BeanPostProcessor postProcessor : postProcessors) {
                postProcessor.postProcessBeforeInitialization(bean, name);
            }
            for (BeanPostProcessor postProcessor : postProcessors) {
                postProcessor.postProcessAfterInitialization(bean, name);
            }
        }
    }

    public Object getBean(String beanName) {
        return beans.get(beanName);
    }
    
    public Set<String> getAllBeans(){
        return beans.keySet();
    }

    public void close() {
        beans.clear();
        postProcessors.clear();
    }
}