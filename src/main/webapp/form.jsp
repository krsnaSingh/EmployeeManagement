<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <title>Employee Form</title>
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            background-color: green;
        }
        table {
            background-color: white;
            padding: 20px;       
        }
        td {
            padding: 10px;
        }
        h2 {
            text-align: center;
        }
        #errorMessages {
            color: red;
            text-align: center;
        }
        input[type="submit"] {
            margin-left: 35%;
            cursor: pointer;
        }
    </style>
</head>
<body>
    <table>
        <tr>
            <td colspan="2">
                <h2>${not empty employee ? "Edit Employee Form" : "Add New Employee Form"}</h2>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <div id="errorMessages">
                    <c:if test="${not empty errorMessage}">
                        <p>${errorMessage}</p>
                    </c:if>
                </div>
            </td>
        </tr>
        <form action="employee" method="post">
            <tr>
                <td><label for="name">Name:</label></td>
                <td>
                    <input type="text" id="name" name="name" value="${employee != null ? employee.name : ''}" required>
                    <c:if test="${not empty param.name && fn:length(param.name) == 0}">
                        <span style="color: red;">Name is required.</span>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td><label for="age">Age:</label></td>
                <td>
                    <input type="number" id="age" name="age" value="${employee != null ? employee.age : ''}" required min="18" max="65">
                    <c:if test="${not empty param.age}">
                        <c:if test="${param.age < 18 || param.age > 65}">
                            <span style="color: red;">Age must be between 18 and 65.</span>
                        </c:if>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td><label for="salary">Salary:</label></td>
                <td>
                    <input type="number" id="salary" name="salary" value="${employee != null ? employee.salary : ''}" required min="0" step="0.01">
                    <c:if test="${not empty param.salary}">
                        <c:if test="${param.salary < 0}">
                            <span style="color: red;">Salary must be non-negative.</span>
                        </c:if>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td><label for="birthDate">Birth Date:</label></td>
                <td>
                    <input type="date" id="birthDate" name="birthDate" value="${employee != null ? employee.birthDate : ''}" required>
                
                </td>
            </tr>
            <tr>
                <td><label>Skills:</label></td>
                <td>
                    <input type="checkbox" name="skills" value="Java" <c:if test="${employee != null and employee.skills.contains('Java')}">checked</c:if>> Java<br>
                    <input type="checkbox" name="skills" value="Python" <c:if test="${employee != null and employee.skills.contains('Python')}">checked</c:if>> Python<br>
                    <input type="checkbox" name="skills" value="C++" <c:if test="${employee != null and employee.skills.contains('C++')}">checked</c:if>> C++<br>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="hidden" name="action" value="${employee != null ? 'update' : 'insert'}">
                    <input type="hidden" name="employeeId" value="${employee != null ? employee.employeeId : ''}">
                    <input type="submit" value="${employee != null ? 'Update' : 'Submit'}">
                </td>
            </tr>
        </form>
        <tr>
            <td colspan="2" style="text-align: center;">
                <a href="employee">View List</a>
            </td>
        </tr>
    </table>
</body>
</html>
