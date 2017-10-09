package com.brianlukonsolo.utility;

import com.brianlukonsolo.forex.ForexPriceRecord;
import com.brianlukonsolo.converters.StringToForexPriceRecordConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ForexPricesTestData {
    private static StringToForexPriceRecordConverter stringToForexPriceRecordConverter = new StringToForexPriceRecordConverter();

    public static final String testData = "2017.09.20,21:20,1.19137,1.19137,1.18847,1.18940,3354\r\n" +
            "2017.09.20,21:25,1.18935,1.19070,1.18875,1.19024,2649\r\n" +
            "2017.09.25,13:50,1.17805,1.17825,1.17790,1.17791,606\r\n" +
            "2017.09.25,13:55,1.17791,1.17826,1.17783,1.17783,665\r\n" +
            "2017.09.25,14:00,1.17782,1.17788,1.17765,1.17767,429\r\n" +
            "2017.09.26,14:05,1.17766,1.17783,1.17756,1.17765,383\r\n" +
            "2017.09.26,14:10,1.17767,1.17816,1.17761,1.17786,628\r\n" +
            "2017.09.26,14:15,1.17786,1.17840,1.17777,1.17810,617\r\n" +
            "2017.09.26,17:45,1.18148,1.18202,1.18139,1.18191,927\r\n" +
            "2017.09.27,17:50,1.18191,1.18202,1.18102,1.18161,1060\r\n" +
            "2017.09.27,17:55,1.18162,1.18246,1.18137,1.18198,1409\r\n" +
            "2017.09.27,18:00,1.18205,1.18225,1.18158,1.18159,1201\r\n" +
            "2017.09.27,18:05,1.18160,1.18210,1.18158,1.18175,644\r\n" +
            "2017.09.28,18:10,1.18175,1.18207,1.18147,1.18149,715\r\n" +
            "2017.09.28,23:25,1.18201,1.18222,1.18198,1.18216,107\r\n" +
            "2017.09.28,23:30,1.18215,1.18231,1.18194,1.18197,235\r\n" +
            "2017.09.28,23:35,1.18196,1.18196,1.18174,1.18175,153\r\n" +
            "2017.09.29,23:40,1.18174,1.18179,1.18143,1.18152,190\r\n" +
            "2017.09.29,23:45,1.18154,1.18169,1.18143,1.18162,440\r\n" +
            "2017.09.29,23:50,1.18161,1.18165,1.18139,1.18160,558\r\n" +
            "2017.09.29,23:55,1.18162,1.18172,1.18095,1.18154,350\r\n";

    public static ArrayList<ForexPriceRecord> createForexPriceRecordArrayListForTesting(){
        String[] forexPriceRecordStrings = testData.split("\r\n");
        ArrayList<ForexPriceRecord> forexPriceRecordsForTesting = new ArrayList<>();
        for(String s : forexPriceRecordStrings){
            ForexPriceRecord forexPriceRecord = createPriceRecordObjectFromString(s);
            forexPriceRecordsForTesting.add(forexPriceRecord);
        }
        return forexPriceRecordsForTesting;
    }

    private static ForexPriceRecord createPriceRecordObjectFromString(String priceRecordString) throws ArrayIndexOutOfBoundsException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDate date;
        LocalTime time;
        float open, high, low, close;
        int volume;
        String[] lineParts = priceRecordString.split(",");

        date = LocalDate.parse(lineParts[0], dateTimeFormatter);
        time = LocalTime.parse(lineParts[1]);
        open = Float.parseFloat(lineParts[2]);
        high = Float.parseFloat(lineParts[3]);
        low = Float.parseFloat(lineParts[4]);
        close = Float.parseFloat(lineParts[5]);
        volume = Integer.parseInt(lineParts[6]);
        ForexPriceRecord recordObject = new ForexPriceRecord(date, time, open, high, low, close, volume);

        return recordObject;

    }
}
