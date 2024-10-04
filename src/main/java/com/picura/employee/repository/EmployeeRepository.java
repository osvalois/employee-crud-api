package com.picura.employee.repository;

import com.picura.employee.model.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {

    @Query("{ 'nombre': { $regex: ?0, $options: 'i' } }")
    Flux<Employee> findByNombreContainingIgnoreCase(String nombre);

    @Query("{ 'puesto': ?0 }")
    Flux<Employee> findByPuesto(String puesto);

    @Query("{ 'salario': { $gte: ?0, $lte: ?1 } }")
    Flux<Employee> findBySalarioBetween(double minSalario, double maxSalario);

    @Query("{ 'fechaContratacion': { $gte: ?0, $lte: ?1 } }")
    Flux<Employee> findByFechaContratacionBetween(LocalDate startDate, LocalDate endDate);

    @Meta(allowDiskUse = true)
    @Query(value = "{}", sort = "{ 'salario': -1 }")
    Flux<Employee> findAllOrderBySalarioDesc(Pageable pageable);

    @Query(value = "{ 'departamento': ?0 }", count = true)
    Mono<Long> countByDepartamento(String departamento);

    @Query("{ 'email': ?0 }")
    Mono<Employee> findByEmail(String email);

    @Query(value = "{ $and: [ {'fechaContratacion': { $gte: ?0 }}, {'puesto': ?1} ] }")
    Flux<Employee> findByFechaContratacionAfterAndPuesto(LocalDate date, String puesto);

    @Query("{ 'puesto': ?0 }")
    Flux<Employee> findTop5ByPuestoOrderBySalarioDesc(String puesto, Pageable pageable);
}