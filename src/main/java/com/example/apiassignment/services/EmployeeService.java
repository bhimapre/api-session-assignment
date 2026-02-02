package com.example.apiassignment.services;

import com.example.apiassignment.dto.EmployeeDto;
import com.example.apiassignment.entities.Employee;
import com.example.apiassignment.mapper.EmployeeMapper;
import com.example.apiassignment.repositories.EmployeeRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.FetchNotFoundException;
import org.hibernate.annotations.NotFound;
import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final ModelMapper modelMapper;

    // Find all the Employees
    public List<EmployeeDto> getAllEmployees()
    {
        List<Employee> employees = employeeRepo.findAll();
        return employees.stream()
                .map(emp -> modelMapper.map(emp, EmployeeDto.class))
                .toList();
    }

    // Get Employee By ID
    public EmployeeDto getEmployeeById(UUID employee_id){
        Employee employee = employeeRepo.findById(employee_id).orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        return modelMapper.map(employee, EmployeeDto.class);
    }

    // Add Employee
    public EmployeeDto createEmployee(EmployeeDto employeeDto){
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        employee = employeeRepo.save(employee);
        return modelMapper.map(employee, EmployeeDto.class);
    }

    // Update Employee
    public EmployeeDto updateEmployee(UUID employee_id, EmployeeDto empDto)
    {
        Employee employee = employeeRepo.findById(employee_id).orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        employee.setName(empDto.getName());
        employee.setEmail(empDto.getEmail());
        employee.setPhone_number(empDto.getPhone_number());
        employee.setDepartment(empDto.getDepartment());
        employee.setRole(empDto.getRole());
        employee.setCity(empDto.getCity());
        employee.setAddress(empDto.getAddress());

        Employee updateEmployee = employeeRepo.save(employee);
        return modelMapper.map(updateEmployee, EmployeeDto.class);
    }

    // Delete Employee
//    public EmployeeDto deleteEmployee(UUID employee_id){
//        Employee employee = employeeRepo.findById(employee_id).orElseThrow(() -> new RuntimeException("Employee not found"));
//          employeeRepo.deleteById(employee_id);
//    }

    // Update Employee Using Patch
    public EmployeeDto updateEmployeeUsingPatch(UUID employee_id)
    {
        Employee employee = employeeRepo.findById(employee_id).orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        employee.setAddress(employee.getAddress());

        Employee updateEmployee = employeeRepo.save(employee);
        return modelMapper.map(updateEmployee, EmployeeDto.class);
    }
}
