package org.test.di.factory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
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
import org.test.di.annotations.Scope;
import org.test.di.config.BeanPostProcessor;
import org.test.di.service.Cache;
import org.test.di.service.ServiceLocator;
import org.test.di.utils.ClassUtil;
import org.test.di.utils.Pair;

public class BeanFactory {
    private static final Logger LOG = LoggerFactory.getLogger(BeanFactory.class);

    private Map<String, Pair<Field, Object>> autowereCangidates = new HashMap<>();
    private List<BeanPostProcessor> postProcessors = new ArrayList<>();
    private List<String> proxyList = new ArrayList<>();
    private ProxyBeanGenerationStrategy proxyStrategy = new ProxyBeanGenerationStrategy();
    private SingletonBeanGenerationStrategy singletonStrategy = new SingletonBeanGenerationStrategy();

    public void addPostProcessor(BeanPostProcessor postProcessor) {
        LOG.info("Registering Bean Post Processors");
        postProcessors.add(postProcessor);
    }

    public void instantiate(String basePackage) {
        try {
            ClassLoader classLoader = ClassUtil.getClassLoader();
            String path = basePackage.replace('.', File.separatorChar);
            Enumeration<URL> resourceUrls = classLoader.getResources(path);
            while (resourceUrls.hasMoreElements()) {
                URL url = resourceUrls.nextElement();
                File file = Paths.get(url.toURI()).toFile();
                for (File classFile : file.listFiles()) {
                    if (classFile.isDirectory()) {
                        LOG.info("We located subderictory - " + classFile.getAbsolutePath());
                        instantiate(basePackage + "." + classFile.getName());
                        continue;
                    }
                    String fileName = classFile.getName();
                    String className = null;
                    if (fileName.endsWith(".class")) {
                        className = fileName.substring(0, fileName.lastIndexOf("."));
                    }
                    Class classObject = Class.forName(basePackage + "." + className);
                    if (classObject.isAnnotationPresent(Component.class)) {
                        LOG.info("Found new Component - " + classObject);
                        Object instance = classObject.newInstance();
                        if (instance instanceof BeanPostProcessor) {
                            BeanPostProcessor postProcessor = (BeanPostProcessor) instance;
                            addPostProcessor(postProcessor);
                            break;
                        }
                        Component component = (Component) classObject.getAnnotation(Component.class);
                        Scope value = component.value();
                        String beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
                        if (value.equals(Scope.PROXY)) {
                            proxyList.add(beanName);
                        }
                        Cache.getInstance().addBean(beanName, instance);
                        for (Field field : classObject.getDeclaredFields()) {
                            if (field.isAnnotationPresent(Autowired.class)) {
                                String fieldName = field.getName();
                                autowereCangidates.put(fieldName, new Pair<>(field, instance));
                            }
                        }
                    }

                }
            }
        } catch (IOException | InstantiationException | IllegalAccessException | ClassNotFoundException | URISyntaxException e) {
            LOG.error(e.toString());
        }
    }

    public void populateProperties() {
        try {
            autowereCangidates.keySet().forEach(value -> LOG.info("Autowire Candidates held in list - " + value));
            for (String beanName : autowereCangidates.keySet()) {
                Pair<Field, Object> pair = autowereCangidates.get(beanName);
                Field field = pair.getLeft();
                LOG.info("Autowiring Field - " + field.toGenericString());
                field.setAccessible(true);
                if (proxyList.contains(beanName)) {
                    Object bean = ServiceLocator.getBean(beanName, proxyStrategy);
                    field.set(pair.getRight(), bean);
                } else {
                    Object bean = ServiceLocator.getBean(beanName, singletonStrategy);
                    field.set(pair.getRight(), bean);
                }
                field.setAccessible(false);
            }
        } catch (IllegalAccessException e) {
            LOG.error(e.toString());
        }
    }

    public void processBeforeBeanInitialization() {
        if (!postProcessors.isEmpty()) {
            for (String name : Cache.getInstance().getAllBeanNames()) {
                Object bean = Cache.getInstance().getBean(name);
                for (BeanPostProcessor postProcessor : postProcessors) {
                    postProcessor.postProcessBeforeInitialization(name, bean);
                }
            }
        }
    }
    
    public void processAfterBeanInitialization() {
        if (!postProcessors.isEmpty()) {
            for (String name : Cache.getInstance().getAllBeanNames()) {
                Object bean = Cache.getInstance().getBean(name);
                for (BeanPostProcessor postProcessor : postProcessors) {
                    postProcessor.postProcessAfterInitialization(name, bean);
                }
            }
        }
    }

    public Object getBean(String beanName) {
        return Cache.getInstance().getBean(beanName);
    }

    public Set<String> getAllBeans() {
        return Cache.getInstance().getAllBeanNames();
    }

    public void close() {
        Cache.getInstance().clear();
        postProcessors.clear();
    }
}