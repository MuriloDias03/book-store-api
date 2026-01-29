package com.murilodias03.bookstore.controllers.docs;

import com.murilodias03.bookstore.data.dto.UploadFileResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileControllerDocs {

    @Operation(summary = "Upload a File",
            description = "Upload a file",
            tags = {"Files"})
    UploadFileResponseDTO uploadFile(MultipartFile file);

    @Operation(summary = "Upload Multiple Files",
            description = "Upload multiple files",
            tags = {"Files"})
    List<UploadFileResponseDTO> uploadMultipleFile(MultipartFile[] files);

    @Operation(summary = "Download a File",
            description = "Download a file",
            tags = {"Files"})
    ResponseEntity<Resource> downloadFile(String fileName, HttpServletRequest request);

}