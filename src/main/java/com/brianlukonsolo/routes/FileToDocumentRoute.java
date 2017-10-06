package com.brianlukonsolo.routes;

import com.brianlukonsolo.configuration.ConfigurationProcessor;
import com.brianlukonsolo.processors.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.DOUBLE_NEWLINE;
import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.NEWLINE;

@Component
public class FileToDocumentRoute extends RouteBuilder{
    @Override
    public void configure() throws Exception {
        from("file:INPUTS?noop=true")
                .log(NEWLINE + "Starting route ..." + NEWLINE)
                .process(new ConfigurationProcessor())
                .process(new ReadFileContentsProcessor())
                .process(new FilterByDateProcessor())
                .process(new FilterByTimePeriodProcessor())
                .process(new FilterBySpecificDaysProcessor())
                .process(new FilterByVolumeProcessor())
                .log("${body}")
                .end();
                //.to("file:OUTPUT");

    }

}
