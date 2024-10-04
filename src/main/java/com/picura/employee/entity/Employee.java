package com.picura.employee.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Document(collection = "employees")
@Schema(description = "Entidad que representa a un empleado")
public class Employee {
    @Id
    @Schema(description = "Identificador único del empleado", example = "123e4567-e89b-12d3-a456-426614174000", accessMode = Schema.AccessMode.READ_ONLY)
    private String id = UUID.randomUUID().toString();

    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre del empleado", example = "Juan Pérez")
    private String nombre;

    @NotBlank(message = "El puesto no puede estar vacío")
    @Schema(description = "Puesto del empleado", example = "Desarrollador Senior")
    private String puesto;

    @Positive(message = "El salario debe ser un número positivo")
    @Schema(description = "Salario del empleado", example = "50000.00")
    private double salario;

    @Schema(description = "Fecha de contratación del empleado", example = "2023-01-15")
    private LocalDate fechaContratacion;
}