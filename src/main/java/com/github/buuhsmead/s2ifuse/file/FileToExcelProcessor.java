package com.github.buuhsmead.s2ifuse.file;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileToExcelProcessor implements org.apache.camel.Processor {

    Logger logger = LoggerFactory.getLogger(FileToExcelProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        java.io.File body = (File) exchange.getIn().getBody();

        logger.info(exchange.getIn().getHeaders().toString());

        exchange.getIn().setBody(body.getClass().toString());
    }
}
