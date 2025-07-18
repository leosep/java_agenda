package com.agenda.contacts.interfaces.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactResponse {
    private Long id;
    private String name;
    private String phone;
    private String email;
}