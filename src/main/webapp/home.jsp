<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Employee List</title>
    <style>
        body {
            
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            padding: 20px;
            background-color: green;
        }

        table {
            width: 50%;
            margin: 20px ;
            background-color: white;
       
        }

        th, td {
            padding: 12px;
            text-align: left;
            border: 1px solid;
        }

        th {
            background-color: green;
            color: white;
            text-align: center;
        }
        
        #newform{
        	color:white;
        }

    </style>
</head>
<body>
    <table>
    <thead>
        <tr>
            <th>S.No</th> 
            <th>Name</th>
            <th>Age</th>
            <th>Salary</th>
            <th>Birth Date</th>
            <th>Skills</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="employee" items="${employeeList}" varStatus="status">
            <tr>
                <td>${status.index + 1}</td>
                <td>${employee.name}</td>
                <td>${employee.age}</td>
                <td>${employee.salary}</td>
                <td>${employee.birthDate}</td>
                <td>
                    <c:forEach var="skill" items="${employee.skills}" varStatus="skillStatus">
                        <c:out value="${skill}" />
                        <c:if test="${!skillStatus.last}">, </c:if> 
                    </c:forEach>
                </td>
                <td>
                    <a href="employee?action=edit&id=${employee.employeeId}">Edit</a> |
                    <a href="employee?action=delete&id=${employee.employeeId}">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
    
    <a  id = "newform" href="employee?action=new" class="add-new">Add New Employee</a>
</body>
</html>
