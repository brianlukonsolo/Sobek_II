package com.brianlukonsolo.processors;

import com.brianlukonsolo.beans.ForexPriceRecord;
import com.brianlukonsolo.converters.StringToForexPriceRecordConverter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Component
public class ReadFileContentsProcessor implements Processor{
    @Autowired
    private StringToForexPriceRecordConverter stringToForexPriceRecordConverter;

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setHeader("priceRecords", priceRecordStringsToPriceRecordObjectsList(exchange));

    }

    private ArrayList<ForexPriceRecord> priceRecordStringsToPriceRecordObjectsList(final Exchange exchange) throws IOException {
        ArrayList<ForexPriceRecord> listOfPriceRecordObjectsFromFile = new ArrayList<>();
        String lineInFile;
        InputStream inputStream = new ByteArrayInputStream(exchange.getIn().getBody().toString().getBytes(StandardCharsets.UTF_8.name()));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exchange.getIn().getBody(InputStream.class)), 1);
        while ((lineInFile = bufferedReader.readLine()) != null) {
            ForexPriceRecord forexPriceRecord = stringToForexPriceRecordConverter.convert(lineInFile);
            listOfPriceRecordObjectsFromFile.add(forexPriceRecord);
        }
        inputStream.close();
        bufferedReader.close();

        return listOfPriceRecordObjectsFromFile;

    }


}
