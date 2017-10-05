package com.brianlukonsolo.converters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.DOUBLE_NEWLINE;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class StringToDateConverterTest {
    private Logger LOGGER = LoggerFactory.getLogger(StringToDateConverter.class);
    @Autowired
    private StringToDateConverter stringToDateConverter;

    @Test
    public void itShouldConvertAValidStringDateToLocalDateObjectFormat(){
        String dateString = "2017.10.07";
        LocalDate actual = stringToDateConverter.convert(dateString);
        LOGGER.info(DOUBLE_NEWLINE + "Actual output: " + actual + DOUBLE_NEWLINE);
        assertNotEquals(actual, null);
    }
}
