package com.brianlukonsolo.processors;

import com.brianlukonsolo.beans.ForexPriceRecord;
import com.brianlukonsolo.converters.StringToTimeConverter;
import com.brianlukonsolo.correction.TimeMidnightCorrector;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;

import static com.brianlukonsolo.constants.CodeConstants.CamelHeaders.HEADER_CAMEL_FILE_NAME_ONLY;
import static com.brianlukonsolo.constants.CodeConstants.CamelHeaders.HEADER_IS_BUY_TRADE_RESULTS;
import static com.brianlukonsolo.constants.CodeConstants.CamelHeaders.HEADER_IS_SELL_TRADE_RESULTS;
import static com.brianlukonsolo.constants.CodeConstants.ExceptionMessageStrings.ERROR_ONLY_BUY_OR_SELL_ALLOWED;
import static com.brianlukonsolo.constants.CodeConstants.PropertyNames.PROPERTY_TRADE_CLOSED_AT_TIME;
import static com.brianlukonsolo.constants.CodeConstants.PropertyNames.PROPERTY_TRADE_OPENED_AT_TIME;
import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.*;

@Component
public class TradePlacementProcessor implements Processor {
    private static final Logger LOGGER = LoggerFactory.getLogger(TradePlacementProcessor.class);
    private StringToTimeConverter stringToTimeConverter = new StringToTimeConverter();
    private String fileNameOfCsvFile = null;

    @Override
    public void process(Exchange exchange) throws Exception {
        String tradeOpenedAt = TimeMidnightCorrector.correct(exchange.getIn().getHeader(PROPERTY_TRADE_OPENED_AT_TIME, String.class));
        String tradeClosedAt = TimeMidnightCorrector.correct(exchange.getIn().getHeader(PROPERTY_TRADE_CLOSED_AT_TIME, String.class));
        fileNameOfCsvFile = exchange.getIn().getHeader(HEADER_CAMEL_FILE_NAME_ONLY, String.class);

        if (tradeOpenedAt != null && tradeClosedAt != null) {
            ArrayList<ForexPriceRecord> listOfForexPriceRecords = (ArrayList<ForexPriceRecord>) exchange.getIn().getBody();
            listOfForexPriceRecords = filterTradesAccordingToTradeOpenAndTradeCloseTimes(tradeOpenedAt, tradeClosedAt, listOfForexPriceRecords);
            boolean isBuyTradeResults = determineResultOfTrades(BUY_TRADE, listOfForexPriceRecords);
            boolean isSellTradeResults = determineResultOfTrades(SELL_TRADE, listOfForexPriceRecords);

            if(isBuyTradeResults == true && isSellTradeResults == true){
                exchange.getIn().setHeader(HEADER_IS_BUY_TRADE_RESULTS, isBuyTradeResults);
                exchange.getIn().setHeader(HEADER_IS_SELL_TRADE_RESULTS, isSellTradeResults);
            }

            listOfForexPriceRecords = null;
            tradeOpenedAt = null;
            tradeClosedAt = null;
        }


    }

    private ArrayList<ForexPriceRecord> filterTradesAccordingToTradeOpenAndTradeCloseTimes(String tradeOpenedAtTime, String tradeClosedAtTime, ArrayList<ForexPriceRecord> listOfForexPriceRecords) {
        LocalTime start = stringToTimeConverter.convert(tradeOpenedAtTime);
        LocalTime end = stringToTimeConverter.convert(tradeClosedAtTime);
        ArrayList<ForexPriceRecord> filteredListOfForexPriceRecords = new ArrayList<>();

        for (ForexPriceRecord forexPriceRecord : listOfForexPriceRecords) {
            if (forexPriceRecord.getTime().equals(start)) {
                filteredListOfForexPriceRecords.add(forexPriceRecord);
            } else if (forexPriceRecord.getTime().equals(end)) {
                filteredListOfForexPriceRecords.add(forexPriceRecord);
            } else {

            }
        }

        start = null;
        end = null;

        return filteredListOfForexPriceRecords;


    }

    private boolean determineResultOfTrades(String buyOrSell, ArrayList<ForexPriceRecord> listOfPriceRecords) throws Exception {
        boolean isResultsFound = false;
        float totalNunberOfTrades = 0.0f;
        float buyTradeWins = 0.0f;
        float buyTradeLosses = 0.0f;
        float buyTradesWinPercentage = 0.0f;
        float sellTradeWins = 0.0f;
        float sellTradeLosses = 0.0f;
        float sellTradesWinPercentage = 0.0f;

        if(fileNameOfCsvFile != null) {
            LOGGER.info(DOUBLE_NEWLINE + TRADE_FILE_TITLE_START + fileNameOfCsvFile + TRADE_FILE_TITLE_END);
        }

        for (ForexPriceRecord record : listOfPriceRecords) {
            //If index is even (there should be a start price record object and an end record object)
            if (listOfPriceRecords.indexOf(record) % 2 != 0 && listOfPriceRecords.indexOf(record) > 0) {
                totalNunberOfTrades = totalNunberOfTrades + 1;
                if (buyOrSell.toUpperCase().equals("SELL")) {
                    //Sell trades
                    if (((listOfPriceRecords.get(listOfPriceRecords.indexOf(record) - 1).getClose() - record.getClose()) > 0)) {
                        LOGGER.info(buyOrSell + TRADE_STRING_WITH_AND_ARROW + record.getDate().getDayOfWeek() + SINGLE_SPACE + record.getDate() + TRADE_STRING_WIN);
                        sellTradeWins++;
                    } else if (((listOfPriceRecords.get(listOfPriceRecords.indexOf(record) - 1).getClose() - record.getClose()) <= 0)) {
                        LOGGER.info(buyOrSell + TRADE_STRING_WITH_AND_ARROW + record.getDate().getDayOfWeek() + SINGLE_SPACE + record.getDate() + TRADE_STRING_LOSS);
                        sellTradeLosses++;
                    }
                } else if (buyOrSell.toUpperCase().equals("BUY")) {
                    //BUY trades
                    if (((listOfPriceRecords.get(listOfPriceRecords.indexOf(record) - 1).getClose() - record.getClose()) < 0)) {
                        LOGGER.info(buyOrSell + TRADE_STRING_WITH_AND_ARROW + record.getDate().getDayOfWeek() + SINGLE_SPACE + record.getDate() + TRADE_STRING_WIN);
                        buyTradeWins++;
                    } else if (((listOfPriceRecords.get(listOfPriceRecords.indexOf(record) - 1).getClose() - record.getClose()) >= 0)) {
                        LOGGER.info(buyOrSell + TRADE_STRING_WITH_AND_ARROW + record.getDate().getDayOfWeek() + SINGLE_SPACE + record.getDate() + TRADE_STRING_LOSS);
                        buyTradeLosses++;
                    }
                } else {
                    throw new Exception(ERROR_ONLY_BUY_OR_SELL_ALLOWED);
                }
            }
        }

        if (buyOrSell.equals("BUY") && totalNunberOfTrades > 0) {
            LOGGER.info("Total number of [" + buyOrSell + "] trades: " + String.format("%.0f", totalNunberOfTrades));
            LOGGER.info("LOSSES  : " + String.format("%.0f", buyTradeLosses));
            LOGGER.info("WINS    : " + String.format("%.0f", buyTradeWins));
            buyTradesWinPercentage = (buyTradeWins / totalNunberOfTrades) * 100;
            LOGGER.info("Win percentage = " + String.format("%.0f", buyTradesWinPercentage) + "%");
        } else if (buyOrSell.equals("SELL") && totalNunberOfTrades > 0) {
            LOGGER.info("Total number of [" + buyOrSell + "] trades: " + String.format("%.0f", totalNunberOfTrades));
            LOGGER.info("LOSSES : " + String.format("%.0f", sellTradeLosses));
            LOGGER.info("WINS   : " + String.format("%.0f", sellTradeWins));
            sellTradesWinPercentage = (sellTradeWins / totalNunberOfTrades) * 100;
            LOGGER.info("Win percentage = " + String.format("%.0f", sellTradesWinPercentage) + "%" + DOUBLE_NEWLINE + NEWLINE);
        }

        if(totalNunberOfTrades > 0){
            isResultsFound = true;
        }

        return isResultsFound;

    }


}
