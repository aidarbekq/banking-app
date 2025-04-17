package com.example.banking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CreateUserRequest {
    @NotBlank(message = "Name must not be blank")
    @Schema(description = "Имя пользователя", example = "Aidar")
    private String name;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email must not be blank")
    @Schema(description = "Email пользователя", example = "aidar@example.com")
    private String email;

    public CreateUserRequest() {}
    public CreateUserRequest(String name, String email) {
        this.name = name;
        this.email = email;
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}