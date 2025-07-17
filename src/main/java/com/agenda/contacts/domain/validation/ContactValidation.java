package com.agenda.contacts.domain.validation;

import com.agenda.contacts.domain.model.Contact;
import com.agenda.contacts.domain.repository.ContactRepository;
import java.util.regex.Pattern;

public class ContactValidation {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@(.+)$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[0-9]{7,15}$" // Permite + y entre 7 y 15 dígitos
    );

    public static void validateContactCreation(Contact contact, ContactRepository repository) {
        if (contact.getName() == null || contact.getName().trim().isEmpty()) {
            throw new ValidationException("El nombre del contacto no puede estar vacío.");
        }
        if (contact.getPhone() == null || contact.getPhone().trim().isEmpty()) {
            throw new ValidationException("El teléfono del contacto no puede estar vacío.");
        }
        if (contact.getEmail() == null || contact.getEmail().trim().isEmpty()) {
            throw new ValidationException("El email del contacto no puede estar vacío.");
        }

        if (!EMAIL_PATTERN.matcher(contact.getEmail()).matches()) {
            throw new ValidationException("Formato de email inválido.");
        }

        if (!PHONE_PATTERN.matcher(contact.getPhone()).matches()) {
            throw new ValidationException("Formato de teléfono inválido. Debe contener solo dígitos y opcionalmente un '+' al inicio.");
        }

        if (repository.existsByEmail(contact.getEmail())) {
            throw new ValidationException("Ya existe un contacto con este email.");
        }
        if (repository.existsByPhone(contact.getPhone())) {
            throw new ValidationException("Ya existe un contacto con este teléfono.");
        }
    }

    public static void validateContactUpdate(Long id, Contact contact, ContactRepository repository) {
        if (contact.getName() != null && contact.getName().trim().isEmpty()) {
            throw new ValidationException("El nombre del contacto no puede estar vacío si se proporciona.");
        }
        if (contact.getPhone() != null && contact.getPhone().trim().isEmpty()) {
            throw new ValidationException("El teléfono del contacto no puede estar vacío si se proporciona.");
        }
        if (contact.getEmail() != null && contact.getEmail().trim().isEmpty()) {
            throw new ValidationException("El email del contacto no puede estar vacío si se proporciona.");
        }

        if (contact.getEmail() != null && !EMAIL_PATTERN.matcher(contact.getEmail()).matches()) {
            throw new ValidationException("Formato de email inválido.");
        }
        if (contact.getPhone() != null && !PHONE_PATTERN.matcher(contact.getPhone()).matches()) {
            throw new ValidationException("Formato de teléfono inválido. Debe contener solo dígitos y opcionalmente un '+' al inicio.");
        }

        // Validación para evitar duplicados en email/phone si se actualizan y pertenecen a otro contacto
        if (contact.getEmail() != null && repository.findByEmail(contact.getEmail()).isPresent() &&
                !repository.findByEmail(contact.getEmail()).get().getId().equals(id)) {
            throw new ValidationException("Ya existe otro contacto con este email.");
        }
        if (contact.getPhone() != null && repository.findByPhone(contact.getPhone()).isPresent() &&
                !repository.findByPhone(contact.getPhone()).get().getId().equals(id)) {
            throw new ValidationException("Ya existe otro contacto con este teléfono.");
        }
    }
}