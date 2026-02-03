package com.murilodias03.bookstore.file.exporter.contract;

import com.murilodias03.bookstore.data.dto.PersonDTO;
import org.springframework.core.io.Resource;

import java.util.List;

public interface FileExporter {

    Resource exportFile(List<PersonDTO> people) throws Exception;
    Resource exportPerson(PersonDTO person) throws Exception;

}