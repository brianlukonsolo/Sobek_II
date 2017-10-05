package com.brianlukonsolo.routes;

import com.brianlukonsolo.processors.ReadFileContentsProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.DOUBLE_NEWLINE;
import static com.brianlukonsolo.constants.CodeConstants.StringRelatedConstants.NEWLINE;

@Component
public class FileToDocumentRoute extends RouteBuilder{
    @Override
    public void configure() throws Exception {
        from("file:INPUTS?noop=true")
                //.log(NEWLINE + "Starting route ..." + NEWLINE)
                .process(new ReadFileContentsProcessor())
                .to("file:OUTPUT");

    }

}
