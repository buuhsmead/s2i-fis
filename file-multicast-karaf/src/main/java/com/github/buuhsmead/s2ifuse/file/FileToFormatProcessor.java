package com.github.buuhsmead.s2ifuse.file;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileToFormatProcessor implements org.apache.camel.Processor {

    Logger logger = LoggerFactory.getLogger(FileToFormatProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info(exchange.getIn().getHeaders().toString());
    }
}
