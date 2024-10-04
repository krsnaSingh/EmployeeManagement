package com.employeemanage.service;

import java.util.ArrayList;
import java.util.List;

import com.employeemanage.dao.EmployeeDAOImpl;
import com.employeemanage.model.Employee;

public class EmployeeServiceImpl implements EmployeeService{
	
    private EmployeeDAOImpl employeeDAO;

    public EmployeeServiceImpl() {
        employeeDAO = new EmployeeDAOImpl();
    }

    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }

    public Employee getEmployeeById(int id) {
        return employeeDAO.getEmployeeById(id);
    }

    public void addEmployee(Employee employee) {
        employeeDAO.addEmployee(employee);
    }

//    public void updateEmployee(Employee employee) {
//        employeeDAO.updateEmployee(employee);
//    }

    public void deleteEmployee(int id) {
        employeeDAO.deleteEmployee(id);
    }
    
    public void updateEmployee(Employee employee) {
       
        employeeDAO.updateEmployee(employee);

        
        List<String> existingSkills = employeeDAO.getSkillsByEmployeeId(employee.getEmployeeId());
        List<String> newSkills = employee.getSkills();

       
        List<String> skillsToAdd = new ArrayList<>();
        List<String> skillsToRemove = new ArrayList<>();

       
        for (String skill : newSkills) {
            if (!existingSkills.contains(skill)) {
                skillsToAdd.add(skill);
            }
        }
        
        employeeDAO.insertSkills(employee.getEmployeeId(), skillsToAdd);

        for (String skill : existingSkills) {
            if (!newSkills.contains(skill)) {
                skillsToRemove.add(skill);
            }
        }

        employeeDAO.deleteSkills(employee.getEmployeeId(), skillsToRemove);
       

           
    }
}
