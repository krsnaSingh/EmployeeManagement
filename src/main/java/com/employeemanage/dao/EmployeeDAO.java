package com.employeemanage.dao;


import com.employeemanage.model.Employee;

import java.util.List;

public interface EmployeeDAO {

    
    List<Employee> getAllEmployees();

    
    Employee getEmployeeById(int employeeId);

    
    void addEmployee(Employee employee);

    
    void updateEmployee(Employee employee);

    
    void deleteEmployee(int employeeId);

    
    void insertSkills(int employeeId, List<String> skills);

   
    void deleteSkills(int employeeId, List<String> skillsToRemove);

    
    List<String> getSkillsByEmployeeId(int employeeId);

    
    void deleteSkillsByEmployeeId(int employeeId);
}
