package com.brianlukonsolo.processors;

import com.brianlukonsolo.forex.ForexPriceRecord;
import com.brianlukonsolo.utility.CamelExchangeFactory;
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

import static com.brianlukonsolo.constants.CodeConstants.PropertyNames.PROPERTY_FILTER_VOLUME_MAX;
import static com.brianlukonsolo.constants.CodeConstants.PropertyNames.PROPERTY_FILTER_VOLUME_MIN;
import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.DOUBLE_NEWLINE;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FilterByVolumeProcessorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilterByVolumeProcessorTest.class);
    @Autowired
    private FilterByVolumeProcessor filterByVolumeProcessor;
    @Autowired
    private CamelContext context;

    @Test
    public void itShouldFilterAnArrayListOfPriceRecordObjectsByTimePeriod() throws Exception {
        Exchange exchange = CamelExchangeFactory.createExchange(context);
        exchange.getIn().setHeader(PROPERTY_FILTER_VOLUME_MIN, 0);
        exchange.getIn().setHeader(PROPERTY_FILTER_VOLUME_MAX, 250);
        exchange.getIn().setBody(ForexPricesTestData.createForexPriceRecordArrayListForTesting());
        filterByVolumeProcessor.process(exchange);
        ArrayList<ForexPriceRecord> actual = (ArrayList<ForexPriceRecord>) exchange.getIn().getBody();
        LOGGER.info(DOUBLE_NEWLINE + "Actual output: " + actual + DOUBLE_NEWLINE);
        assertEquals(4, actual.size());
    }
}
