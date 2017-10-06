package com.brianlukonsolo.processors;

import com.brianlukonsolo.beans.ForexPriceRecord;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static com.brianlukonsolo.constants.CodeConstants.PropertyNames.PROPERTY_FILTER_VOLUME_MAX;
import static com.brianlukonsolo.constants.CodeConstants.PropertyNames.PROPERTY_FILTER_VOLUME_MIN;

@Component
public class FilterByVolumeProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Integer filterVolumeMin = exchange.getIn().getHeader(PROPERTY_FILTER_VOLUME_MIN, Integer.class);
        Integer filterVolumeMax = exchange.getIn().getHeader(PROPERTY_FILTER_VOLUME_MAX, Integer.class);

        if(filterVolumeMin != null && filterVolumeMax != null && filterVolumeMax != 0){
            ArrayList<ForexPriceRecord> listOfForexPriceRecords = (ArrayList<ForexPriceRecord>) exchange.getIn().getBody();
            listOfForexPriceRecords = filterByVolume(filterVolumeMin, filterVolumeMax, listOfForexPriceRecords);
            exchange.getIn().setBody(listOfForexPriceRecords);

            listOfForexPriceRecords = null;
            filterVolumeMin = null;
            filterVolumeMax = null;
        }


    }

    private ArrayList<ForexPriceRecord> filterByVolume(int minimumVolume, int maximumVolume, ArrayList<ForexPriceRecord> listOfForexPriceRecordObjects){
        ArrayList<ForexPriceRecord> newListOfRecords = new ArrayList<>();
        for (ForexPriceRecord record : listOfForexPriceRecordObjects) {
            if (record.getVolume() >= minimumVolume && record.getVolume() <= maximumVolume) {
                newListOfRecords.add(record);
            }
        }
        return newListOfRecords;


    }
}
