package com.brianlukonsolo.configuration;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

import static com.brianlukonsolo.constants.CodeConstants.LocationsAndPaths.CONFIGURATION_FILE_PATH;
import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.DOUBLE_NEWLINE;
import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.NEWLINE;

@Component
public class ConfigurationProcessor implements Processor{
    private static boolean hasDisplayedConfiguration = false;
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationProcessor.class);
    @Override
    public void process(Exchange exchange) throws Exception {
        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();
        Properties properties = applicationConfiguration.getProperties(CONFIGURATION_FILE_PATH);
        String[] propertyNamesList = properties.stringPropertyNames().toString().replace("[","").replace("]","").split(",");
        propertyNamesList = trimAllStrings(propertyNamesList);

        for(String property : propertyNamesList) {
            exchange.getIn().setHeader(property.trim(), properties.getProperty(property));
        }

        if(hasDisplayedConfiguration == false) {
            LOGGER.info("#=========[-> SOBEK_II CONFIGURATION SETTINGS <-]========#");
            for (String propertyString : propertyNamesList) {
                LOGGER.info("Configuration option : " + propertyString + " = " + properties.getProperty(propertyString.trim()));
            }
            LOGGER.info("#========================================================#");
            LOGGER.info(DOUBLE_NEWLINE);
            hasDisplayedConfiguration = true;
        }

        applicationConfiguration = null;
        properties = null;
        propertyNamesList = null;

    }

    private String[] trimAllStrings(String[] arrayOfStringsToTrim){
        String[] trimmedStringsArray = new String[arrayOfStringsToTrim.length];
         for(int i=0;i<arrayOfStringsToTrim.length;i++){
             trimmedStringsArray[i] = arrayOfStringsToTrim[i].trim();
         }

        return trimmedStringsArray;

    }

}
