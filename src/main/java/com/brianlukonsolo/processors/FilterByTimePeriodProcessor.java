package com.brianlukonsolo.processors;

import com.brianlukonsolo.beans.ForexPriceRecord;
import com.brianlukonsolo.converters.StringToTimeConverter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;

import static com.brianlukonsolo.constants.CodeConstants.PropertyNames.PROPERTY_FILTER_TIME_START;
import static com.brianlukonsolo.constants.CodeConstants.PropertyNames.PROPERTY_FILTER_TIME_STOP;

@Component
public class FilterByTimePeriodProcessor implements Processor{
    private StringToTimeConverter stringToTimeConverter = new StringToTimeConverter();
    @Override
    public void process(Exchange exchange) throws Exception {
        String filterTimeStart = validateTimeInput(exchange.getIn().getHeader(PROPERTY_FILTER_TIME_START, String.class));
        String filterTimeStop = validateTimeInput(exchange.getIn().getHeader(PROPERTY_FILTER_TIME_STOP, String.class));

        if(filterTimeStart != null && filterTimeStop != null){
            ArrayList<ForexPriceRecord> listOfForexPriceRecords = (ArrayList<ForexPriceRecord>) exchange.getIn().getBody();
            listOfForexPriceRecords = filterByTime(filterTimeStart, filterTimeStop, listOfForexPriceRecords);
            exchange.getIn().setBody(listOfForexPriceRecords);
        }
    }

    private String validateTimeInput(String timeString){
        String updatedTimeString = timeString;
        if(timeString.equals("24:00")){
            updatedTimeString = "00:00";
        }
        return updatedTimeString;

    }

    private ArrayList<ForexPriceRecord> filterByTime(String startTime, String endTime, ArrayList<ForexPriceRecord> listOfForexPriceRecordObjects){
        LocalTime start = stringToTimeConverter.convert(startTime);
        LocalTime end = stringToTimeConverter.convert(endTime);
        ArrayList<ForexPriceRecord> filteredList = new ArrayList();

        for(ForexPriceRecord forexPriceRecord : listOfForexPriceRecordObjects){
            if(forexPriceRecord.getTime().equals(start)){
                filteredList.add(forexPriceRecord);
            }else if(forexPriceRecord.getTime().isAfter(start) && forexPriceRecord.getTime().isBefore(end)){
                filteredList.add(forexPriceRecord);
            }else if(forexPriceRecord.getTime().equals(end)){
                filteredList.add(forexPriceRecord);
            }else{

            }
        }
        return filteredList;

    }


}
