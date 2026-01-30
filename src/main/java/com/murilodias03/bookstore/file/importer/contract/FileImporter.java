package com.murilodias03.bookstore.file.importer.contract;

import com.murilodias03.bookstore.data.dto.PersonDTO;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream) throws Exception;

}