package com.brianlukonsolo.processors;

import com.brianlukonsolo.beans.ForexPriceRecord;
import com.brianlukonsolo.converters.StringToForexPriceRecordConverter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class ReadFileContentsProcessor implements Processor {
    private StringToForexPriceRecordConverter stringToForexPriceRecordConverter = new StringToForexPriceRecordConverter();
    @Override
    public void process(Exchange exchange) throws Exception {
        String[] listOfForexPriceRecordStrings = exchange.getIn().getBody(String.class).split("\r\n");
        ArrayList<ForexPriceRecord> forexPriceRecords = priceRecordStringsToPriceRecordObjectsList(listOfForexPriceRecordStrings);
        listOfForexPriceRecordStrings = null;
        exchange.getIn().setBody(forexPriceRecords);

    }

    private ArrayList<ForexPriceRecord> priceRecordStringsToPriceRecordObjectsList(String[] listOfForexPriceRecordStrings) {
        ArrayList<ForexPriceRecord> listOfPriceRecordObjects = new ArrayList<>();
        for(String forexPriceRecordString : listOfForexPriceRecordStrings){
            listOfPriceRecordObjects.add(stringToForexPriceRecordConverter.convert(forexPriceRecordString.trim()));
        }
        System.out.println("NUMBER OF THINGS IN ARRAY LIST = " + listOfPriceRecordObjects.size());
        return listOfPriceRecordObjects;

    }


}
