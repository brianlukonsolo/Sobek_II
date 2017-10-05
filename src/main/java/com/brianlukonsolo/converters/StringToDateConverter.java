package com.brianlukonsolo.converters;

import org.apache.camel.Converter;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@Converter
public class StringToDateConverter {
    @Converter
    public LocalDate convert(String dateString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDate date = LocalDate.parse(dateString, dateTimeFormatter);
        return date;

    }

}
