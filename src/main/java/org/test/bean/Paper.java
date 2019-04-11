package org.test.bean;

import org.test.di.annotations.Component;
import org.test.di.annotations.Scope;

@Component(value = Scope.SINGLETON)
public class Paper implements Printable{
    private String text;
    @Override
    public void setText(String text) {
        this.text = text;
    }
    public String getText(){
        return text;
    }
}
