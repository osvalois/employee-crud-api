package com.picura.employee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Representación de un empleado para transferencia de datos")
public class EmployeeDTO {

    @Schema(description = "ID único del empleado", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(description = "Nombre del empleado", example = "Juan Pérez")
    private String nombre;

    @NotBlank(message = "El puesto es obligatorio")
    @Schema(description = "Puesto del empleado", example = "Desarrollador Senior")
    private String puesto;

    @NotNull(message = "El salario es obligatorio")
    @Positive(message = "El salario debe ser un valor positivo")
    @Schema(description = "Salario del empleado", example = "50000.00")
    private double salario;

    @NotNull(message = "La fecha de contratación es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha de contratación del empleado", example = "2023-01-15")
    private LocalDate fechaContratacion;
}