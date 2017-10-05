package com.brianlukonsolo.processors;

import com.brianlukonsolo.beans.ForexPriceRecord;
import com.brianlukonsolo.factories.CamelExchangeFactory;
import com.brianlukonsolo.utility.ForexPricesTestData;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.ArrayList;

import static com.brianlukonsolo.constants.CodeConstants.CamelHeaders.HEADER_PRICE_RECORDS;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FilterByDateProcessorTest {
    @Autowired
    private FilterByDateProcessor filterByDateProcessor;
    @Autowired
    private CamelContext context;

    @Test
    public void itShouldFilterAnArrayListOfPriceRecordObjectsByDate() throws Exception {
        Exchange exchange = CamelExchangeFactory.createExchange(context);
        exchange.getIn().setHeader(HEADER_PRICE_RECORDS, ForexPricesTestData.createForexPriceRecordArrayListForTesting());
        filterByDateProcessor.process(exchange);
        ArrayList<ForexPriceRecord> actual = (ArrayList<ForexPriceRecord>) exchange.getIn().getHeader(HEADER_PRICE_RECORDS);
        for(ForexPriceRecord forexPriceRecord : actual){
            System.out.println(forexPriceRecord.toString());
        }
    }
}
