package com.murilodias03.bookstore.services;

import com.murilodias03.bookstore.controllers.PersonController;
import com.murilodias03.bookstore.data.dto.PersonDTO;
import com.murilodias03.bookstore.exceptions.BadRequestException;
import com.murilodias03.bookstore.exceptions.FileStorageException;
import com.murilodias03.bookstore.exceptions.RequeriedObjectsIsNullException;
import com.murilodias03.bookstore.exceptions.ResourceNotFoundException;

import static com.murilodias03.bookstore.mapper.ObjectMapper.parseObject;

import com.murilodias03.bookstore.file.exporter.contract.PersonExporter;
import com.murilodias03.bookstore.file.exporter.factory.FileExporterFactory;
import com.murilodias03.bookstore.file.importer.contract.FileImporter;
import com.murilodias03.bookstore.file.importer.factory.FileImporterFactory;
import com.murilodias03.bookstore.model.Person;
import com.murilodias03.bookstore.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class PersonService {

    private final Logger logger = Logger.getLogger(PersonService.class.getName());
    private final PersonRepository personRepository;
    private final FileImporterFactory fileImporter;
    private final FileExporterFactory fileExporter;

    private final PagedResourcesAssembler<PersonDTO> assembler;

    public PersonService(PersonRepository personRepository,
                         FileImporterFactory fileImporter,
                         FileExporterFactory fileExporter,
                         PagedResourcesAssembler<PersonDTO> assembler) {
        this.personRepository = personRepository;
        this.fileImporter = fileImporter;
        this.fileExporter = fileExporter;
        this.assembler = assembler;
    }

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {
        logger.info("Finding all people!");
        var people = personRepository.findAll(pageable);
        return buildPagedModel(pageable, people);
    }

    public PagedModel<EntityModel<PersonDTO>> findByName(String firstName, Pageable pageable) {
        logger.info("Finding people by name!");
        var people = personRepository.findPeopleByName(firstName, pageable);
        return buildPagedModel(pageable, people);
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one person!");

        var entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        var dto = parseObject(entity, PersonDTO.class);

        addHateoasLinks(dto);

        return dto;
    }

    public Resource exportPage(Pageable pageable, String acceptHeader) {
        logger.info("Exporting a people page!");

        var people = personRepository.findAll(pageable)
                .map(person -> parseObject(person, PersonDTO.class))
                .getContent();

        try {
            PersonExporter exporter = fileExporter.getExporter(acceptHeader);
            return exporter.exportPeople(people);
        } catch (Exception e) {
            throw new RuntimeException("Error during file export!", e);
        }
    }

    public Resource exportPerson(Long id, String acceptHeader) {
        logger.info("Exporting data of one person!");

        var person = personRepository.findById(id)
                .map(entity -> parseObject(entity, PersonDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        try {
            PersonExporter exporter = fileExporter.getExporter(acceptHeader);
            return exporter.exportPerson(person);
        } catch (Exception e) {
            throw new RuntimeException("Error during file export!", e);
        }
    }

    public PersonDTO create(PersonDTO person) {
        if (person == null) throw new RequeriedObjectsIsNullException();
        logger.info("Creating one person!");
        var entity = parseObject(person, Person.class);
        var dto = parseObject(personRepository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public List<PersonDTO> createWithFile(MultipartFile file) {
        logger.info("Importing people from file!");

        if (file.isEmpty()) throw new BadRequestException("Please set a valid file!");

        try (InputStream inputStream = file.getInputStream()){
            String fileName = Optional.ofNullable(file.getOriginalFilename())
                    .orElseThrow(() -> new BadRequestException("File name cannot be null!"));

            FileImporter importer = fileImporter.getImporter(fileName);

            List<Person> entities = importer.importFile(inputStream)
                    .stream()
                    .map(dto -> personRepository.save(parseObject(dto, Person.class)))
                    .toList();

            return entities
                .stream()
                .map(entity -> {
                    var dto = parseObject(entity, PersonDTO.class);
                    addHateoasLinks(dto);
                    return dto;
                })
                .toList();

        } catch (Exception e) {
            throw new FileStorageException("Error processing the file!");
        }
    }

    public PersonDTO update(PersonDTO person) {

        if (person == null) throw new RequeriedObjectsIsNullException();

        logger.info("Updating one person!");
        Person entity = personRepository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var dto = parseObject(personRepository.save(entity), PersonDTO.class);

        addHateoasLinks(dto);

        return dto;
    }

    @Transactional
    public PersonDTO disablePerson(Long id) {
        logger.info("Disabling one person!");

        personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        personRepository.disablePerson(id);

        var entity = personRepository.findById(id).get();
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");

        Person entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        personRepository.delete(entity);
    }

    private PagedModel<EntityModel<PersonDTO>> buildPagedModel(Pageable pageable, Page<Person> people) {
        var peopleWithLinks = people.map(person -> {
            var dto = parseObject(person, PersonDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(PersonController.class)
                                .findAll(
                                        pageable.getPageNumber(),
                                        pageable.getPageSize(),
                                        String.valueOf(pageable.getSort())))
                .withSelfRel();

        return assembler.toModel(peopleWithLinks, findAllLink);
    }

    private void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).exportPage(1, 12, "asc", null)).withRel("exportPage").withType("GET").withTitle("Export people"));
        dto.add(linkTo(methodOn(PersonController.class).findByName("", 1, 12, "asc")).withRel("findByName").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class)).slash("createWithFile").withRel("createWithFile").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}