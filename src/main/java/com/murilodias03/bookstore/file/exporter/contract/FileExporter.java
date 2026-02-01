package com.murilodias03.bookstore.file.exporter.contract;

import com.murilodias03.bookstore.data.dto.PersonDTO;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.List;

public interface FileExporter {

    Resource eExporterFile(List<PersonDTO> people) throws Exception;

}