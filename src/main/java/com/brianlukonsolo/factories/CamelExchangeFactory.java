package com.brianlukonsolo.factories;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;

//Singleton
public class CamelExchangeFactory {
    public static CamelExchangeFactory instance = null;
    private CamelExchangeFactory(){ }

    public CamelExchangeFactory getInstance(){
        if(instance == null){
            instance = new CamelExchangeFactory();
        }
        return instance;
    }

    public static Exchange createExchange(CamelContext context) {
        return new DefaultExchange(context);
    }
}
