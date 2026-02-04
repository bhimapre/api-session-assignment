package com.example.apiassignment.services;

import com.example.apiassignment.dto.EmployeeDto;
import com.example.apiassignment.entities.Employee;
import com.example.apiassignment.exception.ResourceNotFoundException;
import com.example.apiassignment.repositories.EmployeeRepo;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final ModelMapper modelMapper;

   @Value("${file.upload}")
   private String uploadDir;

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
        Employee employee = employeeRepo.findById(employee_id).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
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
        Employee employee = employeeRepo.findById(employee_id).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

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
    public void deleteEmployee(UUID employee_id){
        Employee employee = employeeRepo.findById(employee_id).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
          employeeRepo.deleteById(employee_id);
    }

    // Update Employee Using Patch
    public EmployeeDto updateEmployeeUsingPatch(UUID employee_id, String address)
    {
        Employee employee = employeeRepo.findById(employee_id).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        employee.setAddress(address);

        Employee updateEmployee = employeeRepo.save(employee);
        return modelMapper.map(updateEmployee, EmployeeDto.class);
    }

    // Upload Photo
    @CacheEvict(value = "employeePhotos", allEntries = true)
    public String uploadEmployeePhoto(UUID id, MultipartFile file) throws IOException {

        String contentType = file.getContentType();

        if(contentType == null || !(contentType.equals("image/png")
        || contentType.equals("image/jpeg") || contentType.equals("image/jpg")))
        {
            throw new BadRequestException("Only JPG, PNG, JPEG images are allowed");
        }

        if(file.getSize() > 10 *1024 *1024)
        {
            throw new BadRequestException("File size must be less than 10 MB");
        }

        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        String fileName = saveImageToLocal(file);

        employee.setPhoto(fileName);
        employeeRepo.save(employee);
        return fileName;
    }

    private String saveImageToLocal(MultipartFile file) throws IOException {

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    @Cacheable(value = "employeePhotos", key = "#filename")
    public Resource getEmployeePhoto(String filename) throws MalformedURLException
    {
       // System.out.println("image is read from backend");

        Path path = Paths.get(uploadDir).resolve(filename);

        Resource resource = new UrlResource(path.toUri());

        if(!resource.exists())
        {
            throw new ResourceNotFoundException("Photo not found");
        }

        return resource;
    }
}
