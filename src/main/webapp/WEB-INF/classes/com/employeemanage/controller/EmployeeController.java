package com.employeemanage.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class EmployeeController extends HttpServlet  {
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	System.out.println("Inside doPost method of employee");
    	
    	
        // Get form data
        String name = request.getParameter("name");
        String[] skills = request.getParameterValues("skills");
        String ageStr = request.getParameter("age");
        String salaryStr = request.getParameter("salary");
        String birthDateStr = request.getParameter("birthDate");
        

        

        // Use service to add employee to DB
        //employeeService.addEmployee(employee);

        // Redirect or display success message
        //request.setAttribute("message", "Employee added successfully!");
       // request.getRequestDispatcher("index.jsp").forward(request, response);
    }


}
