package com.brianlukonsolo.processors;

import com.brianlukonsolo.factories.CamelExchangeFactory;
import com.brianlukonsolo.utility.ForexPricesTestData;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.brianlukonsolo.constants.CodeConstants.CamelHeaders.HEADER_IS_BUY_TRADE_RESULTS;
import static com.brianlukonsolo.constants.CodeConstants.CamelHeaders.HEADER_IS_SELL_TRADE_RESULTS;
import static com.brianlukonsolo.constants.CodeConstants.PropertyNames.PROPERTY_TRADE_CLOSED_AT_TIME;
import static com.brianlukonsolo.constants.CodeConstants.PropertyNames.PROPERTY_TRADE_OPENED_AT_TIME;
import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.COMMA_SPACE;
import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.DOUBLE_NEWLINE;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TradePlacementProcessorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TradePlacementProcessorTest.class);
    private Exchange exchange;
    @Autowired
    private TradePlacementProcessor tradePlacementProcessor;
    @Autowired
    private CamelContext context;

    @Before
    public void before(){
        exchange = CamelExchangeFactory.createExchange(context);
    }

    @Test
    public void itShouldSetAHeaderAfterDeterminingTheResultsOfABuyOrSellTradesForAGivenListOfForexPriceRecords() throws Exception {
        exchange.getIn().setHeader(PROPERTY_TRADE_OPENED_AT_TIME, "14:00");
        exchange.getIn().setHeader(PROPERTY_TRADE_CLOSED_AT_TIME, "18:00");
        exchange.getIn().setBody(ForexPricesTestData.createForexPriceRecordArrayListForTesting());
        tradePlacementProcessor.process(exchange);
        boolean actualBuy = exchange.getIn().getHeader(HEADER_IS_BUY_TRADE_RESULTS, boolean.class);
        boolean actualSell = exchange.getIn().getHeader(HEADER_IS_SELL_TRADE_RESULTS, boolean.class);
        LOGGER.info(DOUBLE_NEWLINE + "Actual outputs: (isBuyResults, isSellResults) " + actualBuy + COMMA_SPACE + actualSell + DOUBLE_NEWLINE);
        assertEquals(true, actualBuy);
        assertEquals(true, actualSell);
    }

    @Test
    public void itShouldNotSetTradeResulsHeadersIfNoTradesWerePossible() throws Exception {
        exchange.getIn().setHeader(PROPERTY_TRADE_OPENED_AT_TIME, "14:00");
        exchange.getIn().setHeader(PROPERTY_TRADE_CLOSED_AT_TIME, "14:00");
        exchange.getIn().setBody(ForexPricesTestData.createForexPriceRecordArrayListForTesting());
        tradePlacementProcessor.process(exchange);
        boolean actualBuy = exchange.getIn().getHeader(HEADER_IS_BUY_TRADE_RESULTS, boolean.class);
        boolean actualSell = exchange.getIn().getHeader(HEADER_IS_SELL_TRADE_RESULTS, boolean.class);
        LOGGER.info(DOUBLE_NEWLINE + "Actual outputs: (isBuyResults, isSellResults) " + actualBuy + COMMA_SPACE + actualSell + DOUBLE_NEWLINE);
        assertEquals(false, actualBuy);
        assertEquals(false, actualSell);
    }

}
