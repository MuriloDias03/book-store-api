package com.murilodias03.bookstore.file.exporter.factory;

import com.murilodias03.bookstore.exceptions.BadRequestException;
import com.murilodias03.bookstore.file.exporter.MediaTypes;
import com.murilodias03.bookstore.file.exporter.contract.FileExporter;
import com.murilodias03.bookstore.file.exporter.impl.CsvExporter;
import com.murilodias03.bookstore.file.exporter.impl.XlsxExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileExporterFactory {

    private final Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);

    private final ApplicationContext context;

    public FileExporterFactory(ApplicationContext context) {
        this.context = context;
    }

    public FileExporter getExporter(String acceptHeader) throws Exception {
        if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
            return context.getBean(XlsxExporter.class);
        } else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV_VALUE)) {
            return context.getBean(CsvExporter.class);
        } else {
            throw new BadRequestException("Invalid file format!");
        }
    }
}