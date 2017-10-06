package com.brianlukonsolo.correction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.ACTUAL_OUTPUT;
import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.DOUBLE_NEWLINE;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TimeMidnightCorrectorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeMidnightCorrectorTest.class);

    @Test
    public void whenPassedAnInputTimeStringOfTwentyFourHoursItShouldCorrectItToTwentyThreeFiftyNine(){
        String time = "24:00";
        String expected = "23:59";
        String actual = TimeMidnightCorrector.correct(time);
        LOGGER.info(DOUBLE_NEWLINE + ACTUAL_OUTPUT + actual + DOUBLE_NEWLINE);
        assertTrue(actual.equals(expected));
    }
}
