package com.brianlukonsolo.configuration;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

import static com.brianlukonsolo.constants.CodeConstants.LocationsAndPaths.CONFIGURATION_FILE_PATH;
import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.NEWLINE;

@Component
public class ConfigurationProcessor implements Processor{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationProcessor.class);
    @Override
    public void process(Exchange exchange) throws Exception {
        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();
        Properties properties = applicationConfiguration.getProperties(CONFIGURATION_FILE_PATH);
        String[] propertyNamesList = properties.stringPropertyNames().toString().replace("[","").replace("]","").split(",");
        propertyNamesList = trimAllStrings(propertyNamesList);
        for(String property : propertyNamesList) {
            LOGGER.info(NEWLINE + "Configuration option : " + property + " = " + properties.getProperty(property.trim()) + NEWLINE);
            exchange.getIn().setHeader(property.trim(), properties.getProperty(property));

        }

    }

    private String[] trimAllStrings(String[] arrayOfStringsToTrim){
        String[] trimmedStringsArray = new String[arrayOfStringsToTrim.length];
         for(int i=0;i<arrayOfStringsToTrim.length;i++){
             trimmedStringsArray[i] = arrayOfStringsToTrim[i].trim();
         }

        return trimmedStringsArray;

    }

}
