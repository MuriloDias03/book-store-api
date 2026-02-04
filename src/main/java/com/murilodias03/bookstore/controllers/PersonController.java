package com.murilodias03.bookstore.controllers;

import com.murilodias03.bookstore.controllers.docs.PersonControllerDocs;
import com.murilodias03.bookstore.data.dto.PersonDTO;
import com.murilodias03.bookstore.file.exporter.MediaTypes;
import com.murilodias03.bookstore.services.PersonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/person")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController implements PersonControllerDocs {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.ok().body(personService.findAll(pageable));
    }

    @GetMapping(value = "/exportPage", produces = {
            MediaTypes.APPLICATION_XLSX_VALUE,
            MediaTypes.APPLICATION_CSV_VALUE,
            MediaTypes.APPLICATION_PDF_VALUE})
    @Override
    public ResponseEntity<Resource> exportPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));

        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);

        Resource file = personService.exportPage(pageable, acceptHeader);

        Map<String, String> extensionMap = Map.of(
                MediaTypes.APPLICATION_XLSX_VALUE, ".xlsx",
                MediaTypes.APPLICATION_CSV_VALUE, ".csv",
                MediaTypes.APPLICATION_PDF_VALUE, ".pdf"
        );
        var fileExtension = extensionMap.getOrDefault(acceptHeader, "");

        var contentType = acceptHeader != null ? acceptHeader : "application/octet-stream";
        var fileName = "people_exported" + fileExtension;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(file);
    }

    @GetMapping(value = "/exportPerson/{id}", produces = MediaTypes.APPLICATION_PDF_VALUE)
    @Override
    public ResponseEntity<Resource> exportPerson(@PathVariable Long id,
                                                 HttpServletRequest request) {
        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);

        Resource file = personService.exportPerson(id, acceptHeader);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(acceptHeader))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=person.pdf")
                .body(file);
    }

    @GetMapping(value = "/findPeopleByName/{firstName}",
            produces = {
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE,
                MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findByName(
            @PathVariable String firstName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.ok().body(personService.findByName(firstName, pageable));
    }

    @GetMapping(value = "/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE})
    @Override
    public PersonDTO findById(@PathVariable Long id) {
        return personService.findById(id);
    }


    @PostMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public PersonDTO create(@RequestBody PersonDTO person) {
        return personService.create(person);
    }

    @PostMapping(value = "/createWithFile",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public List<PersonDTO> createWithFile(@RequestParam MultipartFile file) {
        return personService.createWithFile(file);
    }

    @PutMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public PersonDTO update(@RequestBody PersonDTO person) {
        return personService.create(person);
    }

    @PatchMapping(value = "/{id}",
            produces = {
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE,
                MediaType.APPLICATION_YAML_VALUE})
    @Override
    public PersonDTO disablePerson(@PathVariable Long id){
        return personService.disablePerson(id);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable Long id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }

}