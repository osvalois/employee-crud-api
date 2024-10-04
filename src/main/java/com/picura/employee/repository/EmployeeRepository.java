package com.picura.employee.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.picura.employee.entity.Employee;

import java.time.LocalDate;

/**
 * Repository interface for Employee entities.
 * This interface extends ReactiveMongoRepository to provide CRUD operations and
 * custom query methods for Employee documents in MongoDB.
 */
public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {

    /**
     * Finds employees whose names contain the given string, ignoring case.
     *
     * @param nombre The name to search for
     * @return A Flux of Employee entities matching the search criteria
     */
    @Query("{ 'nombre': { $regex: ?0, $options: 'i' } }")
    Flux<Employee> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Finds employees by their position.
     *
     * @param puesto The position to search for
     * @return A Flux of Employee entities with the specified position
     */
    @Query("{ 'puesto': ?0 }")
    Flux<Employee> findByPuesto(String puesto);

    /**
     * Finds employees with salaries within a specified range.
     *
     * @param minSalario The minimum salary
     * @param maxSalario The maximum salary
     * @return A Flux of Employee entities with salaries in the specified range
     */
    @Query("{ 'salario': { $gte: ?0, $lte: ?1 } }")
    Flux<Employee> findBySalarioBetween(double minSalario, double maxSalario);

    /**
     * Finds employees hired within a specified date range.
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A Flux of Employee entities hired within the specified date range
     */
    @Query("{ 'fechaContratacion': { $gte: ?0, $lte: ?1 } }")
    Flux<Employee> findByFechaContratacionBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Finds all employees ordered by salary in descending order.
     * This query allows disk use to handle large result sets.
     *
     * @param pageable Pagination information
     * @return A Flux of Employee entities ordered by salary (highest to lowest)
     */
    @Meta(allowDiskUse = true)
    @Query(value = "{}", sort = "{ 'salario': -1 }")
    Flux<Employee> findAllOrderBySalarioDesc(Pageable pageable);

    /**
     * Counts the number of employees in a specific department.
     *
     * @param departamento The department name
     * @return A Mono with the count of employees in the specified department
     */
    @Query(value = "{ 'departamento': ?0 }", count = true)
    Mono<Long> countByDepartamento(String departamento);

    /**
     * Finds an employee by their email address.
     *
     * @param email The email address to search for
     * @return A Mono of the Employee entity with the specified email
     */
    @Query("{ 'email': ?0 }")
    Mono<Employee> findByEmail(String email);

    /**
     * Finds employees hired after a specific date and with a specific position.
     *
     * @param date The date after which employees were hired
     * @param puesto The position to filter by (can be null to ignore this criteria)
     * @return A Flux of Employee entities matching the criteria
     */
    @Query(value = "{ $and: [ {'fechaContratacion': { $gte: ?0 }}, {'puesto': ?1} ] }")
    Flux<Employee> findByFechaContratacionAfterAndPuesto(LocalDate date, String puesto);

    /**
     * Finds the top 5 highest-paid employees in a specific position.
     *
     * @param puesto The position to filter by
     * @param pageable Pagination information (should be set to retrieve only the top 5)
     * @return A Flux of the top 5 Employee entities in the specified position, ordered by salary (highest to lowest)
     */
    @Query("{ 'puesto': ?0 }")
    Flux<Employee> findTop5ByPuestoOrderBySalarioDesc(String puesto, Pageable pageable);
}