package com.example.apiassignment.controllers;

import com.example.apiassignment.dto.EmployeeDto;
import com.example.apiassignment.services.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Value("${file.upload}")
    private String uploadDir;

    // Add Employee
    @PostMapping
    public ResponseEntity<EmployeeDto> AddEmployee(@RequestBody EmployeeDto employeeDto)
    {
        try{
            EmployeeDto emp = employeeService.createEmployee(employeeDto);
            return new ResponseEntity<>(emp, HttpStatus.CREATED);
        }
        catch (Exception e) {
            throw new RuntimeException("Employee creation failed");
        }
    }

    // Get All Employee
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllStudent() {
        try {
            List<EmployeeDto> studentList = employeeService.getAllEmployees();
            return new ResponseEntity<>(studentList, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Getting all employees is failed");
        }
    }

    // Get Employee By ID
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getStudentById(@PathVariable("id") UUID id)
    {
        try {
            EmployeeDto employeeDto = employeeService.getEmployeeById(id);
            return new ResponseEntity<>(employeeDto, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Employee not found");
        }
    }

    // Update Employee
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable("id") UUID id, @RequestBody EmployeeDto employeeDto)
    {
        try{
            EmployeeDto updateEmployee = employeeService.updateEmployee(id, employeeDto);
            return ResponseEntity.ok(updateEmployee);
        } catch (Exception e) {
            throw new RuntimeException("Updating is failed");
        }
    }

    // Delete Employee
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") UUID id) {
//        try {
//            employeeService.deleteEmployee(id);
//            return ResponseEntity.noContent().build();
//        } catch (Exception e) {
//            throw new RuntimeException("Deleting is failed");
//        }
//    }

    // Update Employee Using Patch
    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateUsingPatch(@PathVariable("id") UUID id)
    {
        try{
            EmployeeDto deleteEmployee = employeeService.updateEmployeeUsingPatch(id);
            return ResponseEntity.ok(deleteEmployee);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload picture");
        }
    }
}
