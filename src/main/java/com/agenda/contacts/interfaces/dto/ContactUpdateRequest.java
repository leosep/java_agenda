package com.agenda.contacts.interfaces.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ContactUpdateRequest {
    // @NotBlank no se usa aquí porque los campos pueden ser nulos para una actualización parcial
    private String name;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Formato de teléfono inválido. Debe contener solo dígitos y opcionalmente un '+' al inicio.",
            groups = ValidationGroups.Update.class) // Aplicar validación solo si el campo no es nulo
    private String phone;

    @Email(message = "Formato de email inválido.",
            groups = ValidationGroups.Update.class) // Aplicar validación solo si el campo no es nulo
    private String email;
}