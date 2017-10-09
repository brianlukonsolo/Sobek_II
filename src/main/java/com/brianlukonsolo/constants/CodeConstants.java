package com.brianlukonsolo.constants;

public class CodeConstants {
    public static class LocationsAndPaths {
        public static final String CONFIGURATION_FILE_PATH = "src\\main\\resources\\configuration.properties";
    }

    public static class ExceptionMessageStrings{
        public static final String ERROR_ONLY_BUY_OR_SELL_ALLOWED = "You did not provide a valid Order type! Only BUY or SELL is allowed!";
    }

    public static class PropertyNames {
        public static final String PROPERTY_FILTER_DATE_START = "filterDate_start";
        public static final String PROPERTY_FILTER_DATE_STOP = "filterDate_stop";
        public static final String PROPERTY_FILTER_TIME_START = "filterTime_start";
        public static final String PROPERTY_FILTER_TIME_STOP = "filterTime_stop";
        public static final String PROPERTY_FILTER_VOLUME_MIN = "filterVolume_min";
        public static final String PROPERTY_FILTER_VOLUME_MAX = "filterVolume_max";
        public static final String PROPERTY_FILTER_SPECIFIC_DAYS = "filterDays_specific";
        public static final String PROPERTY_TRADE_OPENED_AT_TIME = "tradeOpenedAtTime";
        public static final String PROPERTY_TRADE_CLOSED_AT_TIME = "tradeClosedAtTime";
        public static final String PROPERTY_TRADES_CONSECUTIVE_INTERVAL = "consecutiveTradesWithInterval";


    }

    public static class StringRelatedConstants {
        public static final String NEWLINE = "\n";
        public static final String DOUBLE_NEWLINE = "\n\n";
        public static final String SINGLE_SPACE = " ";
        public static final String COMMA_SPACE = ", ";
        public static final String ACTUAL_OUTPUT = "Actual output: ";
        public static final String BUY_TRADE = "BUY";
        public static final String SELL_TRADE = "SELL";
        public static final String TRADE_STRING_WITH_AND_ARROW = " Trade --> ";
        public static final String TRADE_STRING_WIN = " --> Result: WIN";
        public static final String TRADE_STRING_LOSS = " --> Result: LOSS";
        public static final String TRADE_FILE_TITLE_START = "#=======[->" + "TRADE RESULTS FOR FILE: ";
        public static final String TRADE_FILE_TITLE_END = " <-]-->";
        public static final String BETWEEN_START_PART = "[ -- Between times: ";
        public static final String BETWEEN_END_PART = " -- ]";
        public static final String AND = "and";
        public static final String CLOSE_PRICES = "close-prices: ";


    }

    public static class CamelHeaders {
        public static final String HEADER_PRICE_RECORDS = "priceRecords";
        public static final String HEADER_BUY_TRADE_RESULT = "buyTradeResult";
        public static final String HEADER_SELL_TRADE_RESULT = "sellTradeResult";
        public static final String HEADER_CAMEL_FILE_NAME_ONLY = "CamelFileNameOnly";
        public static final String HEADER_IS_BUY_TRADE_RESULTS = "isBuyTradeResults";
        public static final String HEADER_IS_SELL_TRADE_RESULTS = "isBuyTradeResults";
        public static final String HEADER_CLOSE_PRICE_DECIMAL_PLACES = "closePriceDecimalPlaces";


    }

}
