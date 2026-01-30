package com.murilodias03.bookstore.file.importer.factory;

import com.murilodias03.bookstore.exceptions.BadRequestException;
import com.murilodias03.bookstore.file.importer.contract.FileImporter;
import com.murilodias03.bookstore.file.importer.impl.CsvImporter;
import com.murilodias03.bookstore.file.importer.impl.XlsxImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileImporterFactory {

    private final Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);

    private final ApplicationContext context;

    public FileImporterFactory(ApplicationContext context) {
        this.context = context;
    }

    public FileImporter getImporter(String fileName) throws Exception {
        if (fileName.endsWith(".xlsx")) {
            return context.getBean(XlsxImporter.class);
        } else if (fileName.endsWith(".csv")) {
            return context.getBean(CsvImporter.class);
        } else {
            throw new BadRequestException("Invalid file format!");
        }
    }
}