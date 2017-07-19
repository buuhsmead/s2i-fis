/**
 *  Copyright 2005-2016 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package com.github.buuhsmead.s2ifuse.file;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * A spring-boot application that includes a Camel route builder to setup the Camel routes
 */
@SpringBootApplication
@ImportResource({"classpath:spring/camel-context.xml"})
public class Application extends RouteBuilder {

    public static final String DIRECT_EXCEL_OUTPUT = "direct:excelOutput";
    public static final String DIRECT_FORMAT_OUTPUT = "direct:formatOutput";

    // must have a main method spring-boot can run
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

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
