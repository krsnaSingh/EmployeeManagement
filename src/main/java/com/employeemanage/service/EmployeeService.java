package com.employeemanage.service;


import java.util.List;
import com.employeemanage.model.Employee;

public interface EmployeeService {
	
    List<Employee> getAllEmployees();
    
    Employee getEmployeeById(int id);
    
    void addEmployee(Employee employee);
    
    void updateEmployee(Employee employee);
    
    void deleteEmployee(int id);
}
