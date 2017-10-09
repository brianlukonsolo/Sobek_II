package com.brianlukonsolo.processors;

import com.brianlukonsolo.forex.ForexPriceRecord;
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
        exchange.getIn().setBody(forexPriceRecords);

        listOfForexPriceRecordStrings = null;
        forexPriceRecords = null;


    }

    private ArrayList<ForexPriceRecord> priceRecordStringsToPriceRecordObjectsList(String[] listOfForexPriceRecordStrings) {
        ArrayList<ForexPriceRecord> listOfPriceRecordObjects = new ArrayList<>();
        for(String forexPriceRecordString : listOfForexPriceRecordStrings){
            listOfPriceRecordObjects.add(stringToForexPriceRecordConverter.convert(forexPriceRecordString.trim()));
        }
        return listOfPriceRecordObjects;

    }


}
