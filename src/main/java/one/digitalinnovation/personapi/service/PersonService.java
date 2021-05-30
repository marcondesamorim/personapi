package one.digitalinnovation.personapi.service;

import lombok.AllArgsConstructor;
import one.digitalinnovation.personapi.dto.mapper.PersonMapper;
import one.digitalinnovation.personapi.dto.request.PersonDTO;
import one.digitalinnovation.personapi.dto.response.MessageResponseDTO;
import one.digitalinnovation.personapi.dto.response.PersonFormattedResponseDTO;
import one.digitalinnovation.personapi.entities.Person;
import one.digitalinnovation.personapi.entities.Phone;
import one.digitalinnovation.personapi.exception.PersonNotFoundException;
import one.digitalinnovation.personapi.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PersonService {

    private PersonRepository personRepository;

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    public MessageResponseDTO createPerson(PersonDTO personDTO) {
        Person personToSave = personMapper.toModel(personDTO);
        Person savedPerson = personRepository.save(personToSave);

        MessageResponseDTO messageResponse = createMessageResponse("Created person with ID ",
                savedPerson.getId());

        return messageResponse;
    }

    public MessageResponseDTO updateById(Long id, PersonDTO personDTO) throws PersonNotFoundException {
        verifyIfExist(id);

        Person personToUpdate = personMapper.toModel(personDTO);
        Person updatePerson = personRepository.save(personToUpdate);

        MessageResponseDTO messageResponse = createMessageResponse("Person successfully updated with ID ",
                updatePerson.getId());

        return messageResponse;
    }

    public void delete(Long id) throws PersonNotFoundException {
        verifyIfExist(id);
        personRepository.deleteById(id);
    }

    public List<PersonDTO> listAll() {
        List<Person> allPeople = personRepository.findAll();
        return allPeople.stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PersonFormattedResponseDTO listAllFormatted() {
        List<Person> allPeople = personRepository.findAll();
        return PersonFormattedResponseDTO.builder().message(toFormattedString(allPeople)).build();
    }

    private List<String> toFormattedString(List<Person> allPeople) {
        List<String> personFormattedString = new ArrayList<String>();

        String aux = "";
        for (Person person : allPeople) {
            aux += "Id: " + person.getId() + ", ";
            aux += "Name: " + person.getFirstName() + " " + person.getLastName() + ", ";
            aux += "CPF: " + person.getCpf() + ", ";
            aux += "Birth Date: " + person.getBirthDate() + ", ";
            List<Phone> listPhone = person.getPhones();
            aux += "Phones: ";
            for (Phone phone : listPhone) {
                aux += phone.getNumber() + " ";
            }
            aux += ", ";
            personFormattedString.add(aux);
        }
        return personFormattedString;
    }

    public PersonDTO findById(Long id) throws PersonNotFoundException {
        Person person = verifyIfExist(id);
        return personMapper.toDTO(person);
    }

    private Person verifyIfExist(Long id) throws PersonNotFoundException {
        return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
    }

    private MessageResponseDTO createMessageResponse(String message, Long id) {
        return MessageResponseDTO.builder()
                .message(message + id)
                .build();
    }

}
