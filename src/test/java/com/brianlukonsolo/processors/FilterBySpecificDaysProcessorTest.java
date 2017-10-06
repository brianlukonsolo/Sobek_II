package com.brianlukonsolo.processors;

import com.brianlukonsolo.beans.ForexPriceRecord;
import com.brianlukonsolo.factories.CamelExchangeFactory;
import com.brianlukonsolo.utility.ForexPricesTestData;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

import static com.brianlukonsolo.constants.CodeConstants.PropertyNames.PROPERTY_FILTER_SPECIFIC_DAYS;
import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.DOUBLE_NEWLINE;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FilterBySpecificDaysProcessorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilterBySpecificDaysProcessor.class);
    @Autowired
    private FilterBySpecificDaysProcessor filterBySpecificDaysProcessor;
    @Autowired
    private CamelContext context;

    @Test
    public void itShouldFilterAnArrayListOfPriceRecordObjectsBySpecificDays() throws Exception {
        Exchange exchange = CamelExchangeFactory.createExchange(context);
        exchange.getIn().setHeader(PROPERTY_FILTER_SPECIFIC_DAYS, "Monday,Tuesday,Friday");
        exchange.getIn().setBody(ForexPricesTestData.createForexPriceRecordArrayListForTesting());
        filterBySpecificDaysProcessor.process(exchange);
        ArrayList<ForexPriceRecord> actual = (ArrayList<ForexPriceRecord>) exchange.getIn().getBody();
        LOGGER.info(DOUBLE_NEWLINE + "Actual output: " + actual + DOUBLE_NEWLINE);
        assertEquals(11,actual.size());
    }
}
