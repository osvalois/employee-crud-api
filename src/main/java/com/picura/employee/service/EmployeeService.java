package com.picura.employee.service;

import com.picura.employee.dto.EmployeeDTO;
import com.picura.employee.mapper.EmployeeMapper;
import com.picura.employee.model.Employee;
import com.picura.employee.repository.EmployeeRepository;
import com.picura.employee.exception.EmployeeNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.time.LocalDate;
import java.util.UUID;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService {
    
    private static final String EMPLOYEE_SERVICE = "employeeService";
    
    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;
    @CircuitBreaker(name = EMPLOYEE_SERVICE, fallbackMethod = "getAllEmployeesFallback")
    @Retry(name = EMPLOYEE_SERVICE)
    @RateLimiter(name = EMPLOYEE_SERVICE)
    @Bulkhead(name = EMPLOYEE_SERVICE)
    @Cacheable(value = "employeesCache", key = "#pageable")
    public Flux<EmployeeDTO> getAllEmployees(Pageable pageable) {
        log.info("Fetching employees page: {}", pageable);
        return employeeRepository.findAll(pageable.getSort())
                .skip(pageable.getPageNumber() * pageable.getPageSize())
                .take(pageable.getPageSize())
                .map(employeeMapper::toDTO)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<EmployeeDTO> getAllEmployeesFallback(Pageable pageable, Throwable t) {
        log.error("Error fetching employees", t);
        return Flux.empty();
    }

    @CircuitBreaker(name = EMPLOYEE_SERVICE, fallbackMethod = "getEmployeeByIdFallback")
    @Retry(name = EMPLOYEE_SERVICE)
    @Cacheable(value = "employeeCache", key = "#id")
    public Mono<EmployeeDTO> getEmployeeById(String id) {
        log.info("Fetching employee with id: {}", id);
        return employeeRepository.findById(id)
                .map(employeeMapper::toDTO)
                .switchIfEmpty(Mono.error(new EmployeeNotFoundException("Employee not found with id: " + id)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<EmployeeDTO> getEmployeeByIdFallback(String id, Throwable t) {
        log.error("Error fetching employee with id: {}", id, t);
        return Mono.empty();
    }

    @CircuitBreaker(name = EMPLOYEE_SERVICE)
    @Retry(name = EMPLOYEE_SERVICE)
    @CacheEvict(value = "employeesCache", allEntries = true)
    public Mono<EmployeeDTO> createEmployee(EmployeeDTO employeeDTO) {
        employeeDTO.setId(UUID.randomUUID().toString());
        log.info("Creating new employee: {}", employeeDTO);
        return Mono.just(employeeDTO)
                .map(employeeMapper::toEntity)
                .flatMap(employeeRepository::save)
                .map(employeeMapper::toDTO)
                .doOnSuccess(e -> log.info("Employee created successfully: {}", e))
                .subscribeOn(Schedulers.boundedElastic());
    }


    @CircuitBreaker(name = EMPLOYEE_SERVICE)
    @Retry(name = EMPLOYEE_SERVICE)
    @CacheEvict(value = {"employeeCache", "employeesCache"}, allEntries = true)
    public Mono<EmployeeDTO> updateEmployee(String id, EmployeeDTO employeeDTO) {
        log.info("Updating employee with id: {}", id);
        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new EmployeeNotFoundException("Employee not found with id: " + id)))
                .flatMap(existingEmployee -> {
                    employeeMapper.updateEntityFromDTO(employeeDTO, existingEmployee);
                    return employeeRepository.save(existingEmployee);
                })
                .map(employeeMapper::toDTO)
                .doOnSuccess(e -> log.info("Employee updated successfully: {}", e))
                .subscribeOn(Schedulers.boundedElastic());
    }


    @CircuitBreaker(name = EMPLOYEE_SERVICE)
    @Retry(name = EMPLOYEE_SERVICE)
    @CacheEvict(value = {"employeeCache", "employeesCache"}, allEntries = true)
    public Mono<Void> deleteEmployee(String id) {
        log.info("Deleting employee with id: {}", id);
        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new EmployeeNotFoundException("Employee not found with id: " + id)))
                .flatMap(employee -> employeeRepository.deleteById(id))
                .doOnSuccess(v -> log.info("Employee deleted successfully with id: {}", id))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @CircuitBreaker(name = EMPLOYEE_SERVICE)
    @Retry(name = EMPLOYEE_SERVICE)
    public Flux<EmployeeDTO> searchEmployees(String query) {
        log.info("Searching employees with query: {}", query);
        return employeeRepository.findByNombreContainingIgnoreCase(query)
                .map(employeeMapper::toDTO)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @CircuitBreaker(name = EMPLOYEE_SERVICE)
    @Retry(name = EMPLOYEE_SERVICE)
    public Mono<EmployeeDTO> promoteEmployee(String id, String newPosition, double salaryIncrease) {
        log.info("Promoting employee with id: {} to position: {} with salary increase: {}", id, newPosition, salaryIncrease);
        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new EmployeeNotFoundException("Employee not found with id: " + id)))
                .flatMap(employee -> {
                    employee.setPuesto(newPosition);
                    employee.setSalario(employee.getSalario() + salaryIncrease);
                    return employeeRepository.save(employee);
                })
                .map(employeeMapper::toDTO)
                .doOnSuccess(e -> log.info("Employee promoted successfully: {}", e))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @CircuitBreaker(name = EMPLOYEE_SERVICE)
    @Retry(name = EMPLOYEE_SERVICE)
    public Flux<EmployeeDTO> getTopEarners(int limit) {
        log.info("Fetching top {} earners", limit);
        return employeeRepository.findAllOrderBySalarioDesc(Pageable.ofSize(limit))
                .map(employeeMapper::toDTO)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @CircuitBreaker(name = EMPLOYEE_SERVICE)
    @Retry(name = EMPLOYEE_SERVICE)
    public Mono<Tuple2<EmployeeDTO, EmployeeDTO>> getEmployeesWithMinMaxSalary() {
        log.info("Fetching employees with minimum and maximum salary");
        return employeeRepository.findAll()
                .collectList()
                .flatMap(employees -> {
                    Employee minSalaryEmployee = employees.stream().min((e1, e2) -> Double.compare(e1.getSalario(), e2.getSalario())).orElseThrow();
                    Employee maxSalaryEmployee = employees.stream().max((e1, e2) -> Double.compare(e1.getSalario(), e2.getSalario())).orElseThrow();
                    return Mono.zip(
                            Mono.just(employeeMapper.toDTO(minSalaryEmployee)),
                            Mono.just(employeeMapper.toDTO(maxSalaryEmployee))
                    );
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @CircuitBreaker(name = EMPLOYEE_SERVICE)
    @Retry(name = EMPLOYEE_SERVICE)
    public Flux<EmployeeDTO> getRecentHires(int months) {
        log.info("Fetching employees hired in the last {} months", months);
        LocalDate cutoffDate = LocalDate.now().minusMonths(months);
        return employeeRepository.findByFechaContratacionAfterAndPuesto(cutoffDate, null)
                .map(employeeMapper::toDTO)
                .subscribeOn(Schedulers.boundedElastic());
    }

    private <T> Function<Throwable, Mono<T>> logAndRethrow(String operation) {
        return throwable -> {
            log.error("Error during {}: {}", operation, throwable.getMessage());
            return Mono.error(throwable);
        };
    }
}