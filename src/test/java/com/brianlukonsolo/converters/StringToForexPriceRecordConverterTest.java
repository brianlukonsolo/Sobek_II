package com.brianlukonsolo.converters;

import com.brianlukonsolo.beans.ForexPriceRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.DOUBLE_NEWLINE;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class StringToForexPriceRecordConverterTest {
    private Logger LOGGER = LoggerFactory.getLogger(StringToForexPriceRecordConverterTest.class);
    @Autowired
    private StringToForexPriceRecordConverter stringToForexPriceRecordConverter;

    @Test
    public void itShouldConvertAPriceRecordStringIntoAPriceRecordObject(){
        String priceRecordString = "2017.09.21,00:00,1.18917,1.18937,1.18890,1.18936,63";
        ForexPriceRecord forexPriceRecord = stringToForexPriceRecordConverter.convert(priceRecordString);
        LOGGER.info(DOUBLE_NEWLINE + "Actual output: " + forexPriceRecord.toString() + DOUBLE_NEWLINE);
        assertTrue(forexPriceRecord.getDate().toString().equals("2017-09-21"));
    }

}
