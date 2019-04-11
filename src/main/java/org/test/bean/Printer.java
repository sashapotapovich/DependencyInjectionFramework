package org.test.bean;

import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.annotations.Scope;

@Component(value = Scope.SINGLETON)
public class Printer {
    
    private static final Logger log = LoggerFactory.getLogger(Printer.class);
    
    @Autowired
    private Paper paper;
    
    public Integer anInt = 0;
    
    public void print(String text){
        paper.setText(text);
        log.info(paper.getText());
    }
    
    
}
