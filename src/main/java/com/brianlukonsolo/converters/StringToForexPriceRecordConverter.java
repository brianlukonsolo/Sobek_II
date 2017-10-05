package com.brianlukonsolo.converters;

import com.brianlukonsolo.beans.ForexPriceRecord;
import org.apache.camel.Converter;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
@Converter
public class StringToForexPriceRecordConverter {

    @Converter
    public static ForexPriceRecord convert(String priceRecordString){
        priceRecordString.trim();
        ForexPriceRecord forexPriceRecord = createPriceRecordObjectFromString(priceRecordString);

        return forexPriceRecord;

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
