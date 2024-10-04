package com.employeemanage.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.employeemanage.model.Employee;

public class EmployeeDAOImpl implements EmployeeDAO {
    private static final Logger LOGGER = Logger.getLogger(EmployeeDAOImpl.class.getName());
    private Connection connection;

    public EmployeeDAOImpl() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://webosmotic-H510M-S2:3306/EmployeeDB", "root", "root");
            LOGGER.info("Database connection successful.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Database connection failed.", e);
        }
    }

    // Get all employees
    public List<Employee> getAllEmployees() {
        List<Employee> employeeList = new ArrayList<>();
        String query = "SELECT * FROM Employee";
        
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(rs.getInt("employeeId"));
                employee.setName(rs.getString("name"));
                employee.setAge(rs.getInt("age"));
                employee.setSalary(rs.getDouble("salary"));
                employee.setBirthDate(rs.getDate("birthDate"));

                employee.setSkills(getSkillsByEmployeeId(employee.getEmployeeId()));
                employeeList.add(employee);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve employees.", e);
        }
        return employeeList;
    }

    // Get employee by ID
    public Employee getEmployeeById(int employeeId) {
        if (employeeId <= 0) {
            LOGGER.warning("Invalid employee ID: " + employeeId);
            return null;
        }
        
        Employee employee = null;
        String query = "SELECT * FROM Employee WHERE employeeId = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                employee = new Employee();
                employee.setEmployeeId(rs.getInt("employeeId"));
                employee.setName(rs.getString("name"));
                employee.setAge(rs.getInt("age"));
                employee.setSalary(rs.getDouble("salary"));
                employee.setBirthDate(rs.getDate("birthDate"));

                employee.setSkills(getSkillsByEmployeeId(employeeId));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve employee with ID: " + employeeId, e);
        }
        return employee;
    }

    // Insert a new employee
    public void addEmployee(Employee employee) {
        if (employee == null) {
            LOGGER.warning("Attempted to add a null employee.");
            return;
        }
        
        String query = "INSERT INTO Employee (name, age, salary, birthDate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, employee.getName());
            ps.setInt(2, employee.getAge());
            ps.setDouble(3, employee.getSalary());
            ps.setDate(4, new java.sql.Date(employee.getBirthDate().getTime()));
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int employeeId = rs.getInt(1);
                insertSkills(employeeId, employee.getSkills());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to add employee: " + employee.getName(), e);
        }
    }

    	// Update an existing employee
    public void updateEmployee(Employee employee) {
    	
        if (employee == null || employee.getEmployeeId() <= 0) {
            LOGGER.warning("Attempted to update a null employee or invalid employee ID.");
            return;
        }

        String query = "UPDATE Employee SET name = ?, age = ?, salary = ?, birthDate = ? WHERE employeeId = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, employee.getName());
            ps.setInt(2, employee.getAge());
            ps.setDouble(3, employee.getSalary());
            ps.setDate(4, new java.sql.Date(employee.getBirthDate().getTime()));
            ps.setInt(5, employee.getEmployeeId());
            ps.executeUpdate();
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to update employee with ID: " + employee.getEmployeeId(), e);
        }
    }

    // Delete specific skills for an employee
    public void deleteSkills(int employeeId, List<String> skillsToRemove) {
    	
    	LOGGER.warning("No of skills to delete " + skillsToRemove.size());
    	
        if (skillsToRemove == null || skillsToRemove.isEmpty()) {
            LOGGER.warning("No skills to delete for employee ID: " + employeeId);
            return;
        }

        String query = "DELETE FROM EmployeeSkills WHERE employeeId = ? AND skill = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (String skill : skillsToRemove) {
                ps.setInt(1, employeeId);
                ps.setString(2, skill);
                ps.addBatch();
            }
            ps.executeBatch();
            LOGGER.info("Deleted skills for employee ID: " + employeeId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to delete skills for employee ID: " + employeeId, e);
        }
    }


    public void deleteEmployee(int employeeId) {
        if (employeeId <= 0) {
            LOGGER.warning("Attempted to delete an employee with invalid ID: " + employeeId);
            return;
        }

        String query = "DELETE FROM Employee WHERE employeeId = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, employeeId);
            ps.executeUpdate();
            LOGGER.info("Deleted employee with ID: " + employeeId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to delete employee with ID: " + employeeId, e);
        }
    }

    // Insert skills for a given employee
    public void insertSkills(int employeeId, List<String> skills) {
    	
    	LOGGER.warning("No of skills to add " + skills.size());
    	
        if (skills == null || skills.isEmpty()) {
            LOGGER.warning("No skills to insert for employee ID: " + employeeId);
            return;
        }

        String query = "INSERT INTO EmployeeSkills (employeeId, skill) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (String skill : skills) {
                ps.setInt(1, employeeId);
                ps.setString(2, skill);
                ps.addBatch();
            }
            ps.executeBatch();
            LOGGER.info("Inserted skills for employee ID: " + employeeId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to insert skills for employee ID: " + employeeId, e);
        }
    }

    // Get skills by employeeId
    public List<String> getSkillsByEmployeeId(int employeeId) {
        List<String> skills = new ArrayList<>();
        String query = "SELECT skill FROM EmployeeSkills WHERE employeeId = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                skills.add(rs.getString("skill"));
            }
          //  LOGGER.info("Retrieved skills for employee ID: " + employeeId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve skills for employee ID: " + employeeId, e);
        }
        return skills;
    }

    // Delete skills by employeeId
    public void deleteSkillsByEmployeeId(int employeeId) {
        String query = "DELETE FROM EmployeeSkills WHERE employeeId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, employeeId);
            ps.executeUpdate();
            LOGGER.info("Deleted skills for employee ID: " + employeeId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to delete skills for employee ID: " + employeeId, e);
        }
    }
}
