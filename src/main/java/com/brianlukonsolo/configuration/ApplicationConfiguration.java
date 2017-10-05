package com.brianlukonsolo.configuration;

import org.springframework.stereotype.Component;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class ApplicationConfiguration {
    public Properties getProperties(String pathToPropertiesFile) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = null;
        inputStream = new FileInputStream(pathToPropertiesFile);
        properties.load(inputStream);
        if(inputStream != null){
            inputStream.close();
        }

        return properties;

    }

}
