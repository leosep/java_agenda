package com.agenda.contacts.interfaces.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ContactCreateRequest {
    @NotBlank(message = "El nombre no puede estar vacío.")
    private String name;

    @NotBlank(message = "El teléfono no puede estar vacío.")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Formato de teléfono inválido. Debe contener solo dígitos y opcionalmente un '+' al inicio.")
    private String phone;

    @NotBlank(message = "El email no puede estar vacío.")
    @Email(message = "Formato de email inválido.")
    private String email;
}