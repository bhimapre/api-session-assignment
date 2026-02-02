package com.example.apiassignment.mapper;

import com.example.apiassignment.dto.EmployeeDto;
import com.example.apiassignment.entities.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public static EmployeeDto toDto(Employee employee)
    {
        if(employee == null)
        {
            return null;
        }

        EmployeeDto dto = new EmployeeDto();
        dto.setEmployee_id(employee.getEmployee_id());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setPhone_number(employee.getPhone_number());
        dto.setDepartment(employee.getDepartment());
        dto.setRole(employee.getRole());
        dto.setCity(employee.getCity());
        dto.setAddress(employee.getAddress());
        return dto;
    }

    public static Employee toEntity(EmployeeDto dto)
    {
        Employee employee = new Employee();
        employee.setEmployee_id(dto.getEmployee_id());
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setPhone_number(dto.getPhone_number());
        employee.setDepartment(dto.getDepartment());
        employee.setRole(dto.getRole());
        employee.setCity(dto.getCity());
        employee.setAddress(dto.getAddress());
        return employee;
    }
}
