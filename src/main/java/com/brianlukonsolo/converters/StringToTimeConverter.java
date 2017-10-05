package com.brianlukonsolo.converters;

import org.apache.camel.Converter;
import org.springframework.stereotype.Component;
import java.time.LocalTime;

@Component
@Converter
public class StringToTimeConverter {
    @Converter
    public LocalTime convert(String timeString) {
        LocalTime time = LocalTime.parse(timeString);
        return time;

    }

}
