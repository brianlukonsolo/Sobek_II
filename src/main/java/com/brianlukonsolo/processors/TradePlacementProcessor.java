package com.brianlukonsolo.processors;

import com.brianlukonsolo.forex.ForexPriceRecord;
import com.brianlukonsolo.converters.StringToTimeConverter;
import com.brianlukonsolo.correction.TimeMidnightCorrector;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;

import static com.brianlukonsolo.constants.CodeConstants.CamelHeaders.*;
import static com.brianlukonsolo.constants.CodeConstants.ExceptionMessageStrings.ERROR_ONLY_BUY_OR_SELL_ALLOWED;
import static com.brianlukonsolo.constants.CodeConstants.PropertyNames.*;
import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.*;

@Component
public class TradePlacementProcessor implements Processor {
    private static final Logger LOGGER = LoggerFactory.getLogger(TradePlacementProcessor.class);
    private StringToTimeConverter stringToTimeConverter = new StringToTimeConverter();
    private String fileNameOfCsvFile = null;
    private String formalDecimalPlaces = "%.5f";

    @Override
    public void process(Exchange exchange) throws Exception {
        String tradeOpenedAt = TimeMidnightCorrector.correct(exchange.getIn().getHeader(PROPERTY_TRADE_OPENED_AT_TIME, String.class));
        String tradeClosedAt = TimeMidnightCorrector.correct(exchange.getIn().getHeader(PROPERTY_TRADE_CLOSED_AT_TIME, String.class));
        String tradesConsecutiveInterval = exchange.getIn().getHeader(PROPERTY_TRADES_CONSECUTIVE_INTERVAL, String.class);
        String[] filterSpecificDaysList = new String[1];
        try{
            trimAllStrings(exchange.getIn().getHeader(PROPERTY_FILTER_SPECIFIC_DAYS, String.class).split(","));
        }catch (NullPointerException e){

        }

        int decimalPlaces = exchange.getIn().getHeader(HEADER_CLOSE_PRICE_DECIMAL_PLACES, int.class);
        if(decimalPlaces > 0){
            formalDecimalPlaces = "%." +decimalPlaces + "f";
        }

        fileNameOfCsvFile = exchange.getIn().getHeader(HEADER_CAMEL_FILE_NAME_ONLY, String.class);

        if (tradeOpenedAt != null && tradeClosedAt != null) {
            ArrayList<ForexPriceRecord> listOfForexPriceRecords = (ArrayList<ForexPriceRecord>) exchange.getIn().getBody();

            if(!tradesConsecutiveInterval.equals("none") && tradesConsecutiveInterval.contains(":")) {
                ArrayList<ArrayList<ForexPriceRecord>> listOfTradeResultsForEachDay = new ArrayList<>();
                for(String day : filterSpecificDaysList){
                    ArrayList<ForexPriceRecord> newListForDay;
                    newListForDay = getForexPriceRecordsForDay(day.trim().toUpperCase(), listOfForexPriceRecords);
                    newListForDay = filterTradesAccordingToSpecifiedTimeInterval(tradesConsecutiveInterval, newListForDay);
                    listOfTradeResultsForEachDay.add(newListForDay);
                }
                //For each day of the week find the results
                boolean isBuyTradeResults = false;
                boolean isSellTradeResults = false;

                for(ArrayList<ForexPriceRecord> listOfPriceRecordsForDay : listOfTradeResultsForEachDay){
                    for(ArrayList<ForexPriceRecord> recordsListForDay : listOfTradeResultsForEachDay){
                        isBuyTradeResults = determineResultOfTrades(BUY_TRADE, recordsListForDay);
                        isSellTradeResults = determineResultOfTrades(SELL_TRADE, recordsListForDay);
                    }
                }

                if (isBuyTradeResults && isSellTradeResults) {
                    exchange.getIn().setHeader(HEADER_IS_BUY_TRADE_RESULTS, isBuyTradeResults);
                    exchange.getIn().setHeader(HEADER_IS_SELL_TRADE_RESULTS, isSellTradeResults);
                }

            } else {
                listOfForexPriceRecords = filterTradesAccordingToTradeOpenAndTradeCloseTimes(tradeOpenedAt, tradeClosedAt, listOfForexPriceRecords);
                boolean isBuyTradeResults = determineResultOfTrades(BUY_TRADE, listOfForexPriceRecords);
                boolean isSellTradeResults = determineResultOfTrades(SELL_TRADE, listOfForexPriceRecords);

                if (isBuyTradeResults && isSellTradeResults) {
                    exchange.getIn().setHeader(HEADER_IS_BUY_TRADE_RESULTS, isBuyTradeResults);
                    exchange.getIn().setHeader(HEADER_IS_SELL_TRADE_RESULTS, isSellTradeResults);
                }

                listOfForexPriceRecords = null;
                tradeOpenedAt = null;
                tradeClosedAt = null;
            }
        }


    }

    private ArrayList<ForexPriceRecord> getForexPriceRecordsForDay(String dayOfTheWeek, ArrayList<ForexPriceRecord> listOfForexPriceObjects){
        ArrayList<ForexPriceRecord> listOfForexPriceRecordsForSpecifiedDay = new ArrayList<>();
        for(ForexPriceRecord forexPriceRecord : listOfForexPriceObjects){
            if(forexPriceRecord.getDate().getDayOfWeek().toString().equals(dayOfTheWeek.toUpperCase().trim())){
                listOfForexPriceRecordsForSpecifiedDay.add(forexPriceRecord);
            }
        }

        return listOfForexPriceRecordsForSpecifiedDay;

    }

    private ArrayList<ForexPriceRecord> filterTradesAccordingToSpecifiedTimeInterval(String timeInterval, ArrayList<ForexPriceRecord> listOfForexPriceRecords){
       LocalTime interval = LocalTime.parse(timeInterval);
       ArrayList<ForexPriceRecord> filteredListOfForexPriceObjects = new ArrayList();
       LocalTime startTimeOfFirstTradeIncrementedByIntervalEachTime;
       //Add first item to the filtered list as a starting point for the algoorithm
       filteredListOfForexPriceObjects.add(listOfForexPriceRecords.get(0));
       startTimeOfFirstTradeIncrementedByIntervalEachTime = filteredListOfForexPriceObjects.get(0).getTime();

       LocalTime marketTradingStartTime = filteredListOfForexPriceObjects.get(0).getTime();
       marketTradingStartTime = marketTradingStartTime.minusHours(interval.getHour());
       marketTradingStartTime = marketTradingStartTime.minusMinutes(interval.getMinute());

        ArrayList<LocalTime> listOfIntervalTimes = new ArrayList<>();
        for(int i=0; i<24;i++){
            if(startTimeOfFirstTradeIncrementedByIntervalEachTime.isAfter(marketTradingStartTime) && startTimeOfFirstTradeIncrementedByIntervalEachTime.isBefore(LocalTime.of(23,59))){
                startTimeOfFirstTradeIncrementedByIntervalEachTime = startTimeOfFirstTradeIncrementedByIntervalEachTime.plusHours(interval.getHour());
                startTimeOfFirstTradeIncrementedByIntervalEachTime = startTimeOfFirstTradeIncrementedByIntervalEachTime.plusMinutes(interval.getMinute());
                listOfIntervalTimes.add(startTimeOfFirstTradeIncrementedByIntervalEachTime);
            }
        }

       for(ForexPriceRecord forexPriceRecord : listOfForexPriceRecords.subList(1,listOfForexPriceRecords.size())){
           startTimeOfFirstTradeIncrementedByIntervalEachTime = startTimeOfFirstTradeIncrementedByIntervalEachTime.plusHours(interval.getHour());
           startTimeOfFirstTradeIncrementedByIntervalEachTime = startTimeOfFirstTradeIncrementedByIntervalEachTime.plusMinutes(interval.getMinute());

           for(LocalTime time : listOfIntervalTimes){
               if(forexPriceRecord.getTime().equals(time)){
                   filteredListOfForexPriceObjects.add(forexPriceRecord);
               }
           }
       }

       return filteredListOfForexPriceObjects;

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
        float totalNumberOfTrades = 0.0f;
        float buyTradeWins = 0.0f;
        float buyTradeLosses = 0.0f;
        float sellTradeWins = 0.0f;
        float sellTradeLosses = 0.0f;
        float buyTradesWinPercentage;
        float sellTradesWinPercentage;

        if (fileNameOfCsvFile != null) {
            LOGGER.info(DOUBLE_NEWLINE + TRADE_FILE_TITLE_START + fileNameOfCsvFile + TRADE_FILE_TITLE_END);
        }

        for (ForexPriceRecord record : listOfPriceRecords) {
            if (listOfPriceRecords.indexOf(record) % 2 != 0 && listOfPriceRecords.indexOf(record) > 0) {
                totalNumberOfTrades = totalNumberOfTrades + 1;
                if (buyOrSell.toUpperCase().equals("SELL")) {
                    //SELL trades
                    if (((listOfPriceRecords.get(listOfPriceRecords.indexOf(record) - 1).getClose() - record.getClose()) > 0)) {
                        LOGGER.info(buyOrSell + TRADE_STRING_WITH_AND_ARROW + record.getDate().getDayOfWeek()  + SINGLE_SPACE + record.getDate() + SINGLE_SPACE + BETWEEN_START_PART + listOfPriceRecords.get(listOfPriceRecords.indexOf(record) - 1).getTime() + SINGLE_SPACE + AND + SINGLE_SPACE + record.getTime() + BETWEEN_END_PART + SINGLE_SPACE + CLOSE_PRICES + SINGLE_SPACE + String.format(formalDecimalPlaces, listOfPriceRecords.get(listOfPriceRecords.indexOf(record) - 1).getClose()) + COMMA_SPACE + String.format(formalDecimalPlaces, record.getClose()) + SINGLE_SPACE + TRADE_STRING_WIN);
                        sellTradeWins++;
                    } else if (((listOfPriceRecords.get(listOfPriceRecords.indexOf(record) - 1).getClose() - record.getClose()) <= 0)) {
                        LOGGER.info(buyOrSell + TRADE_STRING_WITH_AND_ARROW + record.getDate().getDayOfWeek()  + SINGLE_SPACE + record.getDate() + SINGLE_SPACE + BETWEEN_START_PART + listOfPriceRecords.get(listOfPriceRecords.indexOf(record) - 1).getTime() + SINGLE_SPACE + AND + SINGLE_SPACE + record.getTime() + BETWEEN_END_PART + SINGLE_SPACE + CLOSE_PRICES + SINGLE_SPACE + String.format(formalDecimalPlaces, listOfPriceRecords.get(listOfPriceRecords.indexOf(record) - 1).getClose()) + COMMA_SPACE + String.format(formalDecimalPlaces, record.getClose()) + SINGLE_SPACE + TRADE_STRING_LOSS);
                        sellTradeLosses++;
                    }
                } else if (buyOrSell.toUpperCase().equals("BUY")) {
                    //BUY trades
                    if (((listOfPriceRecords.get(listOfPriceRecords.indexOf(record) - 1).getClose() - record.getClose()) < 0)) {
                        LOGGER.info(buyOrSell + TRADE_STRING_WITH_AND_ARROW + record.getDate().getDayOfWeek()  + SINGLE_SPACE + record.getDate() + SINGLE_SPACE + BETWEEN_START_PART + listOfPriceRecords.get(listOfPriceRecords.indexOf(record) - 1).getTime() + SINGLE_SPACE + AND + SINGLE_SPACE + record.getTime() + BETWEEN_END_PART + SINGLE_SPACE + CLOSE_PRICES + SINGLE_SPACE + String.format(formalDecimalPlaces, listOfPriceRecords.get(listOfPriceRecords.indexOf(record) - 1).getClose()) + COMMA_SPACE + String.format(formalDecimalPlaces, record.getClose()) + SINGLE_SPACE + TRADE_STRING_WIN);
                        buyTradeWins++;
                    } else if (((listOfPriceRecords.get(listOfPriceRecords.indexOf(record) - 1).getClose() - record.getClose()) >= 0)) {
                        LOGGER.info(buyOrSell + TRADE_STRING_WITH_AND_ARROW + record.getDate().getDayOfWeek()  + SINGLE_SPACE + record.getDate() + SINGLE_SPACE + BETWEEN_START_PART + listOfPriceRecords.get(listOfPriceRecords.indexOf(record) - 1).getTime() + SINGLE_SPACE + AND + SINGLE_SPACE + record.getTime() + BETWEEN_END_PART + SINGLE_SPACE + CLOSE_PRICES + SINGLE_SPACE + String.format(formalDecimalPlaces, listOfPriceRecords.get(listOfPriceRecords.indexOf(record) - 1).getClose()) + COMMA_SPACE + String.format(formalDecimalPlaces, record.getClose()) + SINGLE_SPACE + TRADE_STRING_LOSS);
                        buyTradeLosses++;
                    }
                } else {
                    throw new Exception(ERROR_ONLY_BUY_OR_SELL_ALLOWED);
                }
            }
        }

        if (buyOrSell.equals("BUY") && totalNumberOfTrades > 0) {
            LOGGER.info("Total number of [" + buyOrSell + "] trades: " + String.format("%.0f", totalNumberOfTrades));
            LOGGER.info("LOSSES  : " + String.format("%.0f", buyTradeLosses));
            LOGGER.info("WINS    : " + String.format("%.0f", buyTradeWins));
            buyTradesWinPercentage = (buyTradeWins / totalNumberOfTrades) * 100;
            LOGGER.info("Win percentage = " + String.format("%.0f", buyTradesWinPercentage) + "%");
        } else if (buyOrSell.equals("SELL") && totalNumberOfTrades > 0) {
            LOGGER.info("Total number of [" + buyOrSell + "] trades: " + String.format("%.0f", totalNumberOfTrades));
            LOGGER.info("LOSSES : " + String.format("%.0f", sellTradeLosses));
            LOGGER.info("WINS   : " + String.format("%.0f", sellTradeWins));
            sellTradesWinPercentage = (sellTradeWins / totalNumberOfTrades) * 100;
            LOGGER.info("Win percentage = " + String.format("%.0f", sellTradesWinPercentage) + "%" + DOUBLE_NEWLINE + NEWLINE);
        }

        if (totalNumberOfTrades > 0) {
            isResultsFound = true;
        }

        return isResultsFound;


    }

    private String[] trimAllStrings(String[] arrayOfStringsToTrim) {
        String[] trimmedStringsArray = new String[arrayOfStringsToTrim.length];
        for (int i = 0; i < arrayOfStringsToTrim.length; i++) {
            trimmedStringsArray[i] = arrayOfStringsToTrim[i].trim();
        }
        return trimmedStringsArray;


    }


}
