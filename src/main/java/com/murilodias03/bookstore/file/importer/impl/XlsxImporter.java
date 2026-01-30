package com.murilodias03.bookstore.file.importer.impl;

import com.murilodias03.bookstore.data.dto.PersonDTO;
import com.murilodias03.bookstore.file.importer.contract.FileImporter;

import java.io.InputStream;
import java.util.List;

public class XlsxImporter implements FileImporter {

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {
        return List.of();
    }

}
