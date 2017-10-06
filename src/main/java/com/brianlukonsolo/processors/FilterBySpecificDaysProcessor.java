package com.brianlukonsolo.processors;

import com.brianlukonsolo.beans.ForexPriceRecord;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static com.brianlukonsolo.constants.CodeConstants.PropertyNames.PROPERTY_FILTER_SPECIFIC_DAYS;

@Component
public class FilterBySpecificDaysProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String[] filterSpecificDaysList = trimAllStrings(exchange.getIn().getHeader(PROPERTY_FILTER_SPECIFIC_DAYS, String.class).split(","));

        if(filterSpecificDaysList != null){
            ArrayList<ForexPriceRecord> listOfForexPriceRecords = (ArrayList<ForexPriceRecord>) exchange.getIn().getBody();
            listOfForexPriceRecords = filterBySpecificDays(listOfForexPriceRecords, filterSpecificDaysList);
            exchange.getIn().setBody(listOfForexPriceRecords);
        }


    }

    private String[] trimAllStrings(String[] arrayOfStringsToTrim){
        String[] trimmedStringsArray = new String[arrayOfStringsToTrim.length];
        for(int i=0;i<arrayOfStringsToTrim.length;i++){
            trimmedStringsArray[i] = arrayOfStringsToTrim[i].trim();
        }
        return trimmedStringsArray;


    }

    private ArrayList<ForexPriceRecord> filterBySpecificDays(ArrayList<ForexPriceRecord> listOfForexPriceRecords, String[] listOfDays){
        ArrayList<ForexPriceRecord> newListOfRecords = new ArrayList<>();
        for (ForexPriceRecord record : listOfForexPriceRecords) {
            for (String day : listOfDays) {
                if (record.getDate().getDayOfWeek().toString().toUpperCase().trim().equals(day.toUpperCase())) {
                    newListOfRecords.add(record);
                }
            }
        }
        return newListOfRecords;


    }


}
