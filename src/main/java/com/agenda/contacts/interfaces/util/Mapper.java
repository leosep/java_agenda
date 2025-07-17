package com.agenda.contacts.interfaces.util;

import com.agenda.contacts.domain.model.Contact;
import com.agenda.contacts.interfaces.dto.ContactCreateRequest;
import com.agenda.contacts.interfaces.dto.ContactResponse;
import com.agenda.contacts.interfaces.dto.ContactUpdateRequest;

public class Mapper {

    public static Contact toDomain(ContactCreateRequest request) {
        return Contact.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .build();
    }

    public static Contact toDomain(ContactUpdateRequest request) {
        return Contact.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .build();
    }

    public static ContactResponse toResponse(Contact contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .name(contact.getName())
                .phone(contact.getPhone())
                .email(contact.getEmail())
                .build();
    }
}