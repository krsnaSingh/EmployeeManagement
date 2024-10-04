package com.employeemanage.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.employeemanage.model.Employee;
import com.employeemanage.service.EmployeeServiceImpl;

public class EmployeeController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(EmployeeController.class.getName());
    private EmployeeServiceImpl employeeService;

    public void init() {
        employeeService = new EmployeeServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";
        
        LOGGER.info("Inside doGet method with action: " + action);
        
        switch (action) {
            case "new":
                showForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteEmployee(request, response);
                break;
            default:
                listEmployees(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Inside doPost method of employee controller");
        
        String action = request.getParameter("action");
        if (action == null) {
            LOGGER.warning("Action is required.");
            response.sendRedirect("home.jsp");
            return;
        }
        
        switch (action) {
            case "insert":
                insertEmployee(request, response);
                break;
            case "update":
                updateEmployee(request, response);
                break;
            default:
                LOGGER.warning("Unknown action: " + action);
                response.sendRedirect("home.jsp");
                break;
        }
    }

    private void listEmployees(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOGGER.info("Listing employees");
        
        List<Employee> employeeList = employeeService.getAllEmployees();
        request.setAttribute("employeeList", employeeList);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
        dispatcher.forward(request, response);
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOGGER.info("Redirecting to form.jsp");
        response.sendRedirect("form.jsp");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        
        if (idParam == null || idParam.isEmpty()) {
            LOGGER.warning("Employee ID is missing for editing");
            request.setAttribute("errorMessage", "Employee ID is required.");
            showForm(request, response);
            return;
        }

        try {
            int employeeId = Integer.parseInt(idParam);
            Employee existingEmployee = employeeService.getEmployeeById(employeeId);
            
            if (existingEmployee == null) {
                LOGGER.warning("No employee found with ID: " + employeeId);
                request.setAttribute("errorMessage", "No employee found with the provided ID.");
                showForm(request, response);
                return;
            }
            
            request.setAttribute("employee", existingEmployee);
            RequestDispatcher dispatcher = request.getRequestDispatcher("form.jsp");
            dispatcher.forward(request, response);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid employee ID format: " + idParam, e);
            request.setAttribute("errorMessage", "Invalid employee ID format.");
            showForm(request, response);
        }
    }

    private void insertEmployee(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        LOGGER.info("Inside insertEmployee method");

        String name = request.getParameter("name");
        String ageParam = request.getParameter("age");
        String salaryParam = request.getParameter("salary");
        String birthDateParam = request.getParameter("birthDate");
        String[] skillsArray = request.getParameterValues("skills");

        if (name == null || name.isEmpty()) {
            LOGGER.warning("Name is required.");
            request.setAttribute("errorMessage", "Name is required.");
            showForm(request, response);
            return;
        }

        if (ageParam == null || ageParam.isEmpty()) {
            LOGGER.warning("Age is required.");
            request.setAttribute("errorMessage", "Age is required.");
            showForm(request, response);
            return;
        }

        if (salaryParam == null || salaryParam.isEmpty()) {
            LOGGER.warning("Salary is required.");
            request.setAttribute("errorMessage", "Salary is required.");
            showForm(request, response);
            return;
        }
        
        if (skillsArray == null || skillsArray.length == 0) {
            LOGGER.warning("Skill is empty.");
            request.setAttribute("errorMessage", "At least one skill is required.");
            showForm(request, response);
            return;
        }

        if (birthDateParam == null || birthDateParam.isEmpty()) {
            LOGGER.warning("Birth date is required.");
            request.setAttribute("errorMessage", "Birth date is required.");
            showForm(request, response);
            return;
        }

        try {
            int age = Integer.parseInt(ageParam);
            double salary = Double.parseDouble(salaryParam);
            Date birthDate = Date.valueOf(birthDateParam);

            if (age < 18 || age > 65) {
                LOGGER.warning("Age must be between 18 and 65.");
                request.setAttribute("errorMessage", "Age must be between 18 and 65.");
                showForm(request, response);
                return;
            }

            if (salary < 0) {
                LOGGER.warning("Salary must be a valid non-negative number.");
                request.setAttribute("errorMessage", "Salary must be a valid non-negative number.");
                showForm(request, response);
                return;
            }

            Employee newEmployee = new Employee();
            newEmployee.setName(name);
            newEmployee.setAge(age);
            newEmployee.setSalary(salary);
            newEmployee.setBirthDate(birthDate);
            newEmployee.setSkills(Arrays.asList(skillsArray)); 

            employeeService.addEmployee(newEmployee);
            LOGGER.info("New employee added: " + newEmployee);

            listEmployees(request, response);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid input values: Age = " + ageParam + ", Salary = " + salaryParam, e);
            request.setAttribute("errorMessage", "Invalid input values: " + e.getMessage());
            showForm(request, response);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Invalid date format for birth date: " + birthDateParam, e);
            request.setAttribute("errorMessage", "Invalid date format for birth date.");
            showForm(request, response);
        }
    }

    private void updateEmployee(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
          LOGGER.info("Inside updateEmployee method");

        String employeeIdParam = request.getParameter("employeeId");
        if (employeeIdParam == null || employeeIdParam.isEmpty()) {
            LOGGER.warning("Employee ID is required for update.");
            request.setAttribute("errorMessage", "Employee ID is required.");
            showForm(request, response);
            return;
        }

        String name = request.getParameter("name");
        String ageParam = request.getParameter("age");
        String salaryParam = request.getParameter("salary");
        String birthDateParam = request.getParameter("birthDate");
        String[] skillsArray = request.getParameterValues("skills");

        if (name == null || name.isEmpty()) {
            LOGGER.warning("Name is required for update.");
            request.setAttribute("errorMessage", "Name is required.");
            showForm(request, response);
            return;
        }

        if (ageParam == null || ageParam.isEmpty()) {
            LOGGER.warning("Age is required for update.");
            request.setAttribute("errorMessage", "Age is required.");
            showForm(request, response);
            return;
        }

        if (salaryParam == null || salaryParam.isEmpty()) {
            LOGGER.warning("Salary is required for update.");
            request.setAttribute("errorMessage", "Salary is required.");
            showForm(request, response);
            return;
        }

        if (skillsArray == null || skillsArray.length == 0) {
            LOGGER.warning("Skill is required for update.");
            request.setAttribute("errorMessage", "At least one skill is required for update.");
            showForm(request, response);
            return;
        }

        try {
            int employeeId = Integer.parseInt(employeeIdParam);
            int age = Integer.parseInt(ageParam);
            double salary = Double.parseDouble(salaryParam);
            Date birthDate = Date.valueOf(birthDateParam);

            if (age < 18 || age > 65) {
                LOGGER.warning("Age must be between 18 and 65.");
                request.setAttribute("errorMessage", "Age must be between 18 and 65.");
                showForm(request, response);
                return;
            }

            if (salary < 0) {
                LOGGER.warning("Salary must be a valid non-negative number.");
                request.setAttribute("errorMessage", "Salary must be a valid non-negative number.");
                showForm(request, response);
                return;
            }

            Employee employee = new Employee();
            employee.setEmployeeId(employeeId);
            employee.setName(name);
            employee.setAge(age);
            employee.setSalary(salary);
            employee.setBirthDate(birthDate);
            employee.setSkills(Arrays.asList(skillsArray));

            employeeService.updateEmployee(employee);
            LOGGER.info("Employee updated: " + employee);

            listEmployees(request, response);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid input values during update: Age = " + ageParam + ", Salary = " + salaryParam, e);
            request.setAttribute("errorMessage", "Invalid input values: " + e.getMessage());
            showForm(request, response);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Invalid date format for birth date: " + birthDateParam, e);
            request.setAttribute("errorMessage", "Invalid date format for birth date.");
            showForm(request, response);
        }
    }

    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        LOGGER.info("Inside deleteEmployee method");

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            LOGGER.warning("Employee ID is required for deletion.");
            request.setAttribute("errorMessage", "Employee ID is required.");
            listEmployees(request, response);
            return;
        }

        try {
            int employeeId = Integer.parseInt(idParam);
            employeeService.deleteEmployee(employeeId);
            LOGGER.info("Employee deleted with ID: " + employeeId);
            listEmployees(request, response);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid employee ID format for deletion: " + idParam, e);
            request.setAttribute("errorMessage", "Invalid employee ID format.");
            listEmployees(request, response);
        }
    }
}
