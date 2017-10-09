package com.brianlukonsolo.processors;

import com.brianlukonsolo.configuration.ConfigurationProcessor;
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

import static com.brianlukonsolo.constants.CodeConstants.PropertyNames.*;
import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.COMMA_SPACE;
import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.DOUBLE_NEWLINE;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ConfigurationProcessorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationProcessorTest.class);
    @Autowired
    private ConfigurationProcessor configurationProcessor;
    @Autowired
    private CamelContext context;

    @Test
    public void itShouldSetAHeaderForEachConfigurationOptionProvidedInTheConfig() throws Exception {
        Exchange exchange = CamelExchangeFactory.createExchange(context);
        configurationProcessor.process(exchange);
        String actualDateStart = exchange.getIn().getHeader(PROPERTY_FILTER_DATE_START, String.class);
        String actualDateStop = exchange.getIn().getHeader(PROPERTY_FILTER_DATE_STOP, String.class);
        String actualTimeStart = exchange.getIn().getHeader(PROPERTY_FILTER_TIME_START, String.class);
        String actualTimeStop = exchange.getIn().getHeader(PROPERTY_FILTER_TIME_STOP, String.class);
        LOGGER.info(DOUBLE_NEWLINE + "Actual data outputs (start,stop): " + actualDateStart + COMMA_SPACE + actualDateStop + DOUBLE_NEWLINE);
        LOGGER.info(DOUBLE_NEWLINE + "Actual time outputs (start, stop): " + actualTimeStart + COMMA_SPACE + actualTimeStop + DOUBLE_NEWLINE);
        assertNotEquals(actualDateStart, null);
        assertNotEquals(actualDateStop, null);
        assertNotEquals(actualTimeStart, null);
        assertNotEquals(actualTimeStop, null);
    }
}
