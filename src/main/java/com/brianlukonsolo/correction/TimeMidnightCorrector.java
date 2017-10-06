package com.brianlukonsolo.correction;

import org.springframework.stereotype.Component;

@Component
public class TimeMidnightCorrector {

    public static String correct(String timeString) {
        //If the user entered 24:00...
        String updatedTimeString = timeString;
        if(timeString.equals("24:00")){
            updatedTimeString = "23:59";
        }
        //If the user entered 9:00 instead of 09:am for example...
        if(timeString.split(":")[0].length() == 1){
            updatedTimeString = "0".concat(timeString);
        }
        return updatedTimeString;
    }
}
