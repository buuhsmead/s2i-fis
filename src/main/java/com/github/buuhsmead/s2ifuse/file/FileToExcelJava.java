package com.github.buuhsmead.s2ifuse.file;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.main.Main;

public final class FileToExcelJava {


    public static void main(String[] args) throws Exception {
        FileToExcelJava fileToExcelJava = new FileToExcelJava();
        fileToExcelJava.boot();
    }

    private void boot() throws Exception {
        // create a Main instance
        Main main = new Main();
        // add routes
        main.addRouteBuilder(new MyRouteBuilder());

        // set the properties from a file
        // main.setPropertyPlaceholderLocations("filetoexcel.properties"); v2.18
        PropertiesComponent pc = new PropertiesComponent();
        pc.setEncoding("utf-8");
        //pc.setLocation("file:com/mycompany/myprop.properties,file:com/mycompany/other.properties");
        pc.setLocation("classpath:filetoexcel.properties");
        main.bind("properties", pc);

        // run until you terminate the JVM
        System.out.println("Starting Camel. Use ctrl + c to terminate the JVM.\n");
        main.run();
    }

    private static class MyRouteBuilder extends RouteBuilder {

        private static final String DIRECT_EXCEL_OUTPUT = "direct:excelOutput";
        private static final String DIRECT_FORMAT_OUTPUT = "direct:formatOutput";

        @Override
        public void configure() throws Exception {
            from("file:{{file_data_inbox}}")
                    .routeId("fromFile")
                    .to("log:com.redhat.consultant.triodos?showAll=true&multiline=true")
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


}
