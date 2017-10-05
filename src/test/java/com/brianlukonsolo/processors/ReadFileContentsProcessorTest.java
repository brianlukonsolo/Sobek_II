package com.brianlukonsolo.processors;

import com.brianlukonsolo.factories.CamelExchangeFactory;
import com.brianlukonsolo.utility.ForexPricesTestData;
import org.apache.camel.Exchange;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

import static com.brianlukonsolo.constants.CodeConstants.CamelHeaders.HEADER_PRICE_RECORDS;
import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.DOUBLE_NEWLINE;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ReadFileContentsProcessorTest extends CamelTestSupport {
    private Logger LOGGER = LoggerFactory.getLogger(ReadFileContentsProcessorTest.class);
    @Autowired
    private ReadFileContentsProcessor readFileContentsProcessor;

    @Test
    public void whenPassedAValidForexCsvItShouldSetBodyAsArrayListOfPriceRecords() throws Exception {
        Exchange exchange = CamelExchangeFactory.createExchange(context);
        exchange.getIn().setBody(ForexPricesTestData.testData);
        readFileContentsProcessor.process(exchange);
        Object actual = exchange.getIn().getBody();
        LOGGER.info(DOUBLE_NEWLINE + "Actual output: " + actual + DOUBLE_NEWLINE);
        assertNotEquals(null, actual);
    }
}
