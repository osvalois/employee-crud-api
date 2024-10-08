package com.picura.employee.controller;

import com.picura.employee.dto.EmployeeDTO;
import com.picura.employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * REST controller for managing Employee resources.
 * This controller provides endpoints for CRUD operations and various employee-related functionalities.
 * It uses reactive programming with Spring WebFlux and includes Swagger annotations for API documentation.
 */
@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employee", description = "API para gestionar empleados")
@Validated
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Retrieves all employees.
     * This endpoint is accessible only to users with ADMIN or HR roles.
     *
     * @return A Flux of EmployeeDTO representing all employees
     */
    @Operation(summary = "Obtener todos los empleados", description = "Retorna un flujo de todos los empleados registrados")
    @ApiResponse(responseCode = "200", description = "Operación exitosa", 
                 content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                 schema = @Schema(implementation = EmployeeDTO.class)))
    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_HR')")
    public Flux<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees(PageRequest.of(0, 10));
    }

    /**
     * Retrieves an employee by their ID.
     * This endpoint is accessible to ADMIN, HR, or the employee themselves.
     *
     * @param id The ID of the employee to retrieve
     * @return A Mono of EmployeeDTO representing the requested employee
     */
    @Operation(summary = "Obtener un empleado por ID", description = "Retorna un empleado basado en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empleado encontrado", 
                     content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = EmployeeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado", 
                     content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_HR') or @securityService.isEmployeeOwner(#id)")
    public Mono<EmployeeDTO> getEmployeeById(
            @Parameter(description = "ID del empleado a buscar") 
            @PathVariable @NotBlank String id) {
        return employeeService.getEmployeeById(id);
    }

    /**
     * Creates a new employee.
     * This endpoint is accessible only to users with ADMIN or HR roles.
     *
     * @param employeeDTO The EmployeeDTO containing the new employee's information
     * @return A Mono of EmployeeDTO representing the created employee
     */
    @Operation(summary = "Crear un nuevo empleado", description = "Crea un nuevo empleado y lo retorna")
    @ApiResponse(responseCode = "201", description = "Empleado creado exitosamente", 
                 content = @Content(mediaType = "application/json", 
                 schema = @Schema(implementation = EmployeeDTO.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_HR')")
    public Mono<EmployeeDTO> createEmployee(
            @Parameter(description = "Empleado a crear") @Valid @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.createEmployee(employeeDTO);
    }

    /**
     * Updates an existing employee.
     * This endpoint is accessible only to users with ADMIN or HR roles.
     *
     * @param id The ID of the employee to update
     * @param employeeDTO The EmployeeDTO containing the updated employee information
     * @return A Mono of EmployeeDTO representing the updated employee
     */
    @Operation(summary = "Actualizar un empleado existente", description = "Actualiza un empleado existente basado en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empleado actualizado exitosamente", 
                     content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = EmployeeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado", 
                     content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_HR')")
    public Mono<EmployeeDTO> updateEmployee(
            @Parameter(description = "ID del empleado a actualizar") @PathVariable @NotBlank String id,
            @Parameter(description = "Datos actualizados del empleado") @Valid @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.updateEmployee(id, employeeDTO);
    }

    /**
     * Deletes an employee.
     * This endpoint is accessible only to users with ADMIN role.
     *
     * @param id The ID of the employee to delete
     * @return A Mono<Void> indicating completion of the operation
     */
    @Operation(summary = "Eliminar un empleado", description = "Elimina un empleado existente basado en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Empleado eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado", 
                     content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Mono<Void> deleteEmployee(
            @Parameter(description = "ID del empleado a eliminar") @PathVariable @NotBlank String id) {
        return employeeService.deleteEmployee(id);
    }

    /**
     * Searches for employees by name.
     * This endpoint is accessible only to users with ADMIN or HR roles.
     *
     * @param query The search query string
     * @return A Flux of EmployeeDTO representing the employees matching the search criteria
     */
    @Operation(summary = "Buscar empleados", description = "Busca empleados por nombre")
    @ApiResponse(responseCode = "200", description = "Operación exitosa", 
                 content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                 schema = @Schema(implementation = EmployeeDTO.class)))
    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_HR')")
    public Flux<EmployeeDTO> searchEmployees(
            @Parameter(description = "Término de búsqueda") @RequestParam String query) {
        return employeeService.searchEmployees(query);
    }

    /**
     * Promotes an employee to a new position with a salary increase.
     * This endpoint is accessible only to users with ADMIN role.
     *
     * @param id The ID of the employee to promote
     * @param newPosition The new position for the employee
     * @param salaryIncrease The amount of salary increase
     * @return A Mono of EmployeeDTO representing the promoted employee
     */
    @Operation(summary = "Promover empleado", description = "Promociona a un empleado a una nueva posición con aumento de salario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empleado promovido exitosamente", 
                     content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = EmployeeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado", 
                     content = @Content)
    })
    @PutMapping("/{id}/promote")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Mono<EmployeeDTO> promoteEmployee(
            @Parameter(description = "ID del empleado a promover") @PathVariable @NotBlank String id,
            @Parameter(description = "Nueva posición") @RequestParam @NotBlank String newPosition,
            @Parameter(description = "Aumento de salario") @RequestParam @Positive double salaryIncrease) {
        return employeeService.promoteEmployee(id, newPosition, salaryIncrease);
    }

    /**
     * Retrieves the top-earning employees.
     * This endpoint is accessible only to users with ADMIN or HR roles.
     *
     * @param limit The number of top earners to retrieve
     * @return A Flux of EmployeeDTO representing the top-earning employees
     */
    @Operation(summary = "Obtener los mejores pagados", description = "Obtiene los empleados con los salarios más altos")
    @ApiResponse(responseCode = "200", description = "Operación exitosa", 
                 content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                 schema = @Schema(implementation = EmployeeDTO.class)))
    @GetMapping("/top-earners")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_HR')")
    public Flux<EmployeeDTO> getTopEarners(
            @Parameter(description = "Límite de resultados") @RequestParam(defaultValue = "5") int limit) {
        return employeeService.getTopEarners(limit);
    }

    /**
     * Retrieves the employees with the minimum and maximum salaries.
     * This endpoint is accessible only to users with ADMIN or HR roles.
     *
     * @return A Mono of Tuple2 containing EmployeeDTO with minimum and maximum salaries
     */
    @Operation(summary = "Obtener empleados con salario mínimo y máximo", description = "Retorna los empleados con el salario más bajo y más alto")
    @ApiResponse(responseCode = "200", description = "Operación exitosa", 
                 content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                 schema = @Schema(implementation = EmployeeDTO.class)))
    @GetMapping("/salary-extremes")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_HR')")
    public Mono<Tuple2<EmployeeDTO, EmployeeDTO>> getEmployeesWithMinMaxSalary() {
        return employeeService.getEmployeesWithMinMaxSalary();
    }

    /**
     * Retrieves recently hired employees.
     * This endpoint is accessible only to users with ADMIN or HR roles.
     *
     * @param months The number of months to consider for recent hires
     * @return A Flux of EmployeeDTO representing recently hired employees
     */
    @Operation(summary = "Obtener contrataciones recientes", description = "Obtiene los empleados contratados en los últimos meses especificados")
    @ApiResponse(responseCode = "200", description = "Operación exitosa", 
                 content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                 schema = @Schema(implementation = EmployeeDTO.class)))
    @GetMapping("/recent-hires")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_HR')")
    public Flux<EmployeeDTO> getRecentHires(
            @Parameter(description = "Número de meses") @RequestParam(defaultValue = "6") int months) {
        return employeeService.getRecentHires(months);
    }
}