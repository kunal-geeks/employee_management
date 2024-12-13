package com.codinghero.employee.services;

import com.codinghero.employee.entity.EmployeeEntity;
import com.codinghero.employee.model.Employee;
import com.codinghero.employee.repository.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        BeanUtils.copyProperties(employee, employeeEntity);
        EmployeeEntity savedEntity = employeeRepository.save(employeeEntity);
        BeanUtils.copyProperties(savedEntity, employee);
        return employee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(entity -> {
                    Employee employee = new Employee();
                    BeanUtils.copyProperties(entity, employee);
                    return employee;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Employee getEmployeeById(Long id) {
        Optional<EmployeeEntity> optionalEntity = employeeRepository.findById(id);
        if (optionalEntity.isEmpty()) {
            throw new RuntimeException("Employee not found with ID: " + id);
        }
        Employee employee = new Employee();
        BeanUtils.copyProperties(optionalEntity.get(), employee);
        return employee;
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        EmployeeEntity existingEntity = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
        existingEntity.setFirstName(employee.getFirstName());
        existingEntity.setLastName(employee.getLastName());
        existingEntity.setEmailId(employee.getEmailId());
        EmployeeEntity updatedEntity = employeeRepository.save(existingEntity);
        BeanUtils.copyProperties(updatedEntity, employee);
        return employee;
    }

    @Override
    public boolean deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found with ID: " + id);
        }
        employeeRepository.deleteById(id);
        return true;
    }
}