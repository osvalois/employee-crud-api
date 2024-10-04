package com.picura.employee.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmployeeNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;
    private final LocalDateTime timestamp;

    public EmployeeNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.timestamp = LocalDateTime.now();
    }

    public EmployeeNotFoundException(String message) {
        super(message);
        this.resourceName = "Employee";
        this.fieldName = "id";
        this.fieldValue = "unknown";
        this.timestamp = LocalDateTime.now();
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}