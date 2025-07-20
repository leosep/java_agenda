package com.agenda.contacts.domain.repository;

import java.util.List;
import java.util.Optional;

import com.agenda.contacts.domain.model.Contact;

// Esta es la interfaz que define las operaciones de persistencia que el dominio necesita
public interface ContactRepository {
    Contact save(Contact contact);
    Optional<Contact> findById(Long id);
    List<Contact> findAll();
    void deleteById(Long id);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<Contact> findByEmail(String email);
    Optional<Contact> findByPhone(String phone);
}