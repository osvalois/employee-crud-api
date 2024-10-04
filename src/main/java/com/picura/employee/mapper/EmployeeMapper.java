package com.picura.employee.mapper;

import com.picura.employee.model.Employee;
import com.picura.employee.dto.EmployeeDTO;
import org.mapstruct.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    Employee toEntity(EmployeeDTO dto);

    EmployeeDTO toDTO(Employee entity);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(EmployeeDTO dto, @MappingTarget Employee entity);


    default Mono<Employee> toEntityMono(Mono<EmployeeDTO> dtoMono) {
        return dtoMono.map(this::toEntity);
    }

    default Mono<EmployeeDTO> toDTOMono(Mono<Employee> entityMono) {
        return entityMono.map(this::toDTO);
    }

    default Flux<EmployeeDTO> toDTOFlux(Flux<Employee> entityFlux) {
        return entityFlux.map(this::toDTO);
    }

    default Flux<Employee> toEntityFlux(Flux<EmployeeDTO> dtoFlux) {
        return dtoFlux.map(this::toEntity);
    }
}