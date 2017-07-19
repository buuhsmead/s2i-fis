package com.github.buuhsmead.s2ifuse.file;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FileToExcelAndFormatRouteBuilder extends RouteBuilder {

    public static final String DIRECT_EXCEL_OUTPUT = "direct:excelOutput";
    public static final String DIRECT_FORMAT_OUTPUT = "direct:formatOutput";


    @Override
    public void configure() throws Exception {
        from("file:{{file_data_inbox}}")
                .routeId("fromFile")
                .to("log:com.github.buuhsmead.s2ifuse.file?showAll=true&multiline=true")
                .log("input file is : ${header.CamelFileAbsolutePath}")
                .multicast()
                .parallelProcessing()
                .to(DIRECT_EXCEL_OUTPUT, DIRECT_FORMAT_OUTPUT);


        from(DIRECT_EXCEL_OUTPUT).routeId("excelOutput")
                .process(new FileToExcelProcessor())
                .log("excel output done.");

        from(DIRECT_FORMAT_OUTPUT).routeId("formatOutput")
                .process(new FileToFormatProcessor())
                .log("format output done.");
    }
}
