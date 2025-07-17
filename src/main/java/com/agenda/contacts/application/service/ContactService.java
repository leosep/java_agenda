package com.agenda.contacts.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.agenda.contacts.domain.model.Contact;
import com.agenda.contacts.domain.repository.ContactRepository;
import com.agenda.contacts.domain.validation.ContactValidation;
import com.agenda.contacts.domain.validation.ValidationException;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact createContact(Contact contact) {
        // Validación de dominio antes de persistir
        ContactValidation.validateContactCreation(contact, contactRepository);
        return contactRepository.save(contact);
    }

    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Contact updateContact(Long id, Contact updatedContactData) {
        Contact existingContact = contactRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Contacto con ID " + id + " no encontrado."));

        // Validar los datos de actualización
        ContactValidation.validateContactUpdate(id, updatedContactData, contactRepository);

        // Actualizar solo los campos proporcionados y no nulos
        existingContact.update(updatedContactData.getName(), updatedContactData.getPhone(), updatedContactData.getEmail());

        return contactRepository.save(existingContact);
    }

    public void deleteContact(Long id) {
        if (!contactRepository.findById(id).isPresent()) {
            throw new ValidationException("Contacto con ID " + id + " no encontrado.");
        }
        contactRepository.deleteById(id);
    }
}