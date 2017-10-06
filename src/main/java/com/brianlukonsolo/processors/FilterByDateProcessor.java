package com.brianlukonsolo.processors;

import com.brianlukonsolo.beans.ForexPriceRecord;
import com.brianlukonsolo.converters.StringToDateConverter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.brianlukonsolo.constants.CodeConstants.PropertyNames.PROPERTY_FILTER_DATE_START;
import static com.brianlukonsolo.constants.CodeConstants.PropertyNames.PROPERTY_FILTER_DATE_STOP;

@Component
public class FilterByDateProcessor implements Processor {
    private StringToDateConverter stringToDateConverter = new StringToDateConverter();

    @Override
    public void process(Exchange exchange) throws Exception {
        String filterStartDate = exchange.getIn().getHeader(PROPERTY_FILTER_DATE_START, String.class);
        String filterEndDate = exchange.getIn().getHeader(PROPERTY_FILTER_DATE_STOP, String.class);

        if (filterStartDate != null && filterEndDate != null) {
            ArrayList<ForexPriceRecord> listOfForexPriceRecords = (ArrayList<ForexPriceRecord>) exchange.getIn().getBody();
            listOfForexPriceRecords = filterByDates(filterStartDate, filterEndDate, listOfForexPriceRecords);
            exchange.getIn().setBody(listOfForexPriceRecords);

            listOfForexPriceRecords = null;
            filterStartDate = null;
            filterEndDate = null;
        }

    }

    private ArrayList<ForexPriceRecord> filterByDates(String startDate, String endDate, ArrayList<ForexPriceRecord> listOfForexPriceRecordObjects) {
        LocalDate start = stringToDateConverter.convert(startDate);
        LocalDate end = stringToDateConverter.convert(endDate);
        ArrayList<ForexPriceRecord> filteredList = new ArrayList<>();

        for (ForexPriceRecord forexPriceRecord : listOfForexPriceRecordObjects) {
            if (forexPriceRecord.getDate().equals(start)) {
                filteredList.add(forexPriceRecord);
            } else if (forexPriceRecord.getDate().isAfter(start) && forexPriceRecord.getDate().isBefore(end)) {
                filteredList.add(forexPriceRecord);
            } else if (forexPriceRecord.getDate().equals(end)) {
                filteredList.add(forexPriceRecord);
            } else {

            }
        }

        start = null;
        end = null;

        return filteredList;

    }

}
