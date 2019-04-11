package org.test.bean;

import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.annotations.Scope;

@Component(value = Scope.SINGLETON)
public class Printer {
    
    private static final Logger LOG = LoggerFactory.getLogger(Printer.class);
    
    @Autowired
    private Paper paper;
    
    public void print(String text){
        paper.setText(text);
        LOG.info(paper.getText());
    }
    
    
}
