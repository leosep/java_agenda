package com.agenda.contacts.infrastructure.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agenda.contacts.domain.model.Contact;
import com.agenda.contacts.domain.repository.ContactRepository;

// JpaContactRepository es una interfaz específica de Spring Data JPA
interface JpaContactRepository extends JpaRepository<Contact, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<Contact> findByEmail(String email);
    Optional<Contact> findByPhone(String phone);
}

@Repository
public class ContactRepositoryAdapter implements ContactRepository {

    private final JpaContactRepository jpaContactRepository;

    public ContactRepositoryAdapter(JpaContactRepository jpaContactRepository) {
        this.jpaContactRepository = jpaContactRepository;
    }

    @Override
    public Contact save(Contact contact) {
        return jpaContactRepository.save(contact);
    }

    @Override
    public Optional<Contact> findById(Long id) {
        return jpaContactRepository.findById(id);
    }

    @Override
    public List<Contact> findAll() {
        return jpaContactRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        jpaContactRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaContactRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return jpaContactRepository.existsByPhone(phone);
    }

    @Override
    public Optional<Contact> findByEmail(String email) {
        return jpaContactRepository.findByEmail(email);
    }

    // @Override
    public Optional<Contact> findByPhone(String phone) {
        return jpaContactRepository.findByPhone(phone);
    }
}