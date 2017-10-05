package com.brianlukonsolo.converters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalTime;

import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.DOUBLE_NEWLINE;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class StringToTimeConverterTest {
    private Logger LOGGER = LoggerFactory.getLogger(StringToTimeConverterTest.class);
    @Autowired
    private StringToTimeConverter stringToTimeConverter;

    @Test
    public void itShouldConvertValidTimeStringsToLocalTimeObjectFormat(){
        String time = "09:00";
        LocalTime actual = stringToTimeConverter.convert(time);
        LOGGER.info(DOUBLE_NEWLINE + "Actual output: " + actual.toString() + DOUBLE_NEWLINE);
        assertNotEquals(actual, null);
    }
}
