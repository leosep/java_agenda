package com.agenda.contacts.interfaces.rest;

import com.agenda.contacts.application.service.ContactService;
import com.agenda.contacts.domain.model.Contact;
import com.agenda.contacts.domain.validation.ValidationException;
import com.agenda.contacts.interfaces.dto.ContactCreateRequest;
import com.agenda.contacts.interfaces.dto.ContactResponse;
import com.agenda.contacts.interfaces.dto.ContactUpdateRequest;
import com.agenda.contacts.interfaces.dto.ValidationGroups;
import com.agenda.contacts.interfaces.util.Mapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/contacts")
@Tag(name = "Contactos", description = "API para la gestión de contactos")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @Operation(summary = "Crear un nuevo contacto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contacto creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContactResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto: contacto con email/teléfono ya existe",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<ContactResponse> createContact(@Valid @RequestBody ContactCreateRequest request) {
        try {
            Contact contact = Mapper.toDomain(request);
            Contact createdContact = contactService.createContact(contact);
            return new ResponseEntity<>(Mapper.toResponse(createdContact), HttpStatus.CREATED);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @Operation(summary = "Obtener un contacto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contacto encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContactResponse.class))),
            @ApiResponse(responseCode = "404", description = "Contacto no encontrado",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> getContactById(@PathVariable Long id) {
        return contactService.getContactById(id)
                .map(contact -> new ResponseEntity<>(Mapper.toResponse(contact), HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contacto con ID " + id + " no encontrado."));
    }

    @Operation(summary = "Obtener todos los contactos")
    @ApiResponse(responseCode = "200", description = "Lista de contactos recuperada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ContactResponse.class)))
    @GetMapping
    public ResponseEntity<List<ContactResponse>> getAllContacts() {
        List<Contact> contacts = contactService.getAllContacts();
        List<ContactResponse> responses = contacts.stream()
                .map(Mapper::toResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Operation(summary = "Actualizar un contacto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contacto actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContactResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Contacto no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto: contacto con email/teléfono ya existe",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ContactResponse> updateContact(@PathVariable Long id,
                                                         @Validated(ValidationGroups.Update.class) @RequestBody ContactUpdateRequest request) {
        try {
            Contact updatedContactData = Mapper.toDomain(request);
            Contact updatedContact = contactService.updateContact(id, updatedContactData);
            return new ResponseEntity<>(Mapper.toResponse(updatedContact), HttpStatus.OK);
        } catch (ValidationException e) {
            if (e.getMessage().contains("no encontrado")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            }
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @Operation(summary = "Eliminar un contacto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contacto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Contacto no encontrado",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        try {
            contactService.deleteContact(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    // Manejo de excepciones para @Valid y ValidationException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}