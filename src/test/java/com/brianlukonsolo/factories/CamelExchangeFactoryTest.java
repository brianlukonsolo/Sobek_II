package com.brianlukonsolo.factories;

import com.brianlukonsolo.utility.CamelExchangeFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CamelExchangeFactoryTest {
    private Logger LOGGER = LoggerFactory.getLogger(CamelExchangeFactoryTest.class);

    @Autowired
    private CamelContext context;

    @Test
    public void itShouldCreateACamelExchange(){
        Exchange exchange = CamelExchangeFactory.createExchange(context);
        LOGGER.info("Actual output: " + exchange);
        assertNotNull(exchange);

    }
}
