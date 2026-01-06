package com.murilodias03.bookstore.services;

import com.murilodias03.bookstore.data.dto.PersonDTO;
import com.murilodias03.bookstore.exceptions.ResourceNotFoundException;

import static com.murilodias03.bookstore.mapper.ObjectMapper.parseListObjects;
import static com.murilodias03.bookstore.mapper.ObjectMapper.parseObject;
import com.murilodias03.bookstore.model.Person;
import com.murilodias03.bookstore.repositories.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonService {

    private final Logger logger = Logger.getLogger(PersonService.class.getName());

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonDTO> findAll() {
        logger.info("Finding all people!");

        return parseListObjects(personRepository.findAll(), PersonDTO.class);
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one person!");

        var entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        return parseObject(entity, PersonDTO.class);
    }

    public PersonDTO create(PersonDTO person) {
        logger.info("Creating one person!");

        var entity = parseObject(person, Person.class);

        return parseObject(personRepository.save(entity), PersonDTO.class);
    }

    public PersonDTO update(PersonDTO person) {
        logger.info("Updating one person!");
        Person entity = personRepository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return parseObject(personRepository.save(entity), PersonDTO.class);
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");

        Person entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        personRepository.delete(entity);
    }
}