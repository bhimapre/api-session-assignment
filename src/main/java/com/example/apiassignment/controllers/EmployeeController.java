package com.example.apiassignment.controllers;

import com.example.apiassignment.dto.EmployeeDto;
import com.example.apiassignment.exception.ResourceNotFoundException;
import com.example.apiassignment.services.EmployeeService;
import com.example.apiassignment.validationGroup.UpdateGroup;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final MessageSource messageSource;

    // Add Employee
    @PostMapping
    public ResponseEntity<EmployeeDto> AddEmployee(@Valid @RequestBody EmployeeDto employeeDto)
    {
            EmployeeDto emp = employeeService.createEmployee(employeeDto);

            return new ResponseEntity<>(emp, HttpStatus.CREATED);
    }

    // Get All Employee
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllStudent()
    {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    // Get Employee By ID
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getStudentById(@PathVariable("id") UUID id)
    {
            EmployeeDto employeeDto = employeeService.getEmployeeById(id);
            return new ResponseEntity<>(employeeDto, HttpStatus.OK);
    }

    // Update Employee
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@Validated(UpdateGroup.class) @PathVariable("id") UUID id, @RequestBody EmployeeDto employeeDto)
    {
            EmployeeDto updateEmployee = employeeService.updateEmployee(id, employeeDto);
            return ResponseEntity.ok(updateEmployee);
    }

    // Delete Employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") UUID id) {
            employeeService.deleteEmployee(id);
            return ResponseEntity.noContent().build();
    }

    // Update Employee Using Patch
    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateUsingPatch(@PathVariable("id") UUID id, @RequestBody Map<String, String> updates)
    {
            EmployeeDto updated = employeeService.updateEmployeeUsingPatch(id, updates.get("address"));
            return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/photo")
    public ResponseEntity<Map<String, String>> uploadEmployeePhoto(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file) throws IOException {

        String fileName = employeeService.uploadEmployeePhoto(id, file);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Photo uploaded successfully");
        response.put("fileName", fileName);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/photo/{filename}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String filename) throws MalformedURLException
    {
        Resource resource = employeeService.getEmployeePhoto(filename);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
    }
}
