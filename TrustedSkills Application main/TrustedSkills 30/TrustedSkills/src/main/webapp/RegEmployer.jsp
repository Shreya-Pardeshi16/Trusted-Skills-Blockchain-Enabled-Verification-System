<%@page import="java.util.Vector"%>
<%@page import="models.JavaFuns"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" href="css/bootstrap.min.css">
 <link rel="stylesheet" href="css/cust.css">

<title> </title>
</head>
<body><jsp:include page="Top.jsp"></jsp:include>
<% try{ response.setHeader("Pragma", "No-cache");
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setDateHeader("Expires", -1);
if(session.getAttribute("userid")==null)
{
	response.sendRedirect("index.jsp");
} 

String userid=String.valueOf(session.getAttribute("userid"));

if(!userid.equalsIgnoreCase("null")){	
	
session.setMaxInactiveInterval(10*60);
JavaFuns jf=new JavaFuns();
Vector v=jf.getValue("select branchname from branches", 1);
%><div class="row">
<div class="col-md-6">

<h2>Register Employer</h2>
<form name="frm" method="post" action="RegEmp" enctype="multipart/form-data">
<table class="tblform">
	<!-- <tr>
		<td>Branch ID</td>
		<td><input type="text" name="branchid" required></td>
	</tr> -->
		<tr>
		<td>Title</td>
		<td><input type="text" class="form-control" name="title" required>
		<input type="hidden" name="usertype" value="employer"/>
		</td>
	</tr>
		<tr><td>Userid</td>
	<td><input type="text" name="userid" class="form-control" required></td>
	</tr>
	 
	<tr><td>Password</td>
	<td><input type="password" name="pswd" class="form-control" required></td>
	</tr>
	<tr>
		<td>About</td>
		<td><textarea class="form-control" name="about" required></textarea></td>
	</tr>
	<tr>
		<td>Address</td>
		<td><textarea class="form-control" name="addr" required></textarea></td>
	</tr>
	<tr>
		<td>Contact Person Name</td>
		<td><input type="text" class="form-control" name="name" required></td>
	</tr>
	
     <tr><td>Mobile Number</td>
       <td><input type="text" name="mobileno"  pattern="^\d{10}$" class="form-control" required></td></tr>
     <tr>
  <td>Email Address</td>       
     <td><input type="text" name="emailid" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$"    class="form-control" required>
      
     </td>
     </tr>
     <tr><td>Logo</td>
     <td>
     <input type="file" name="file" class="form-control"/>
     </td></tr>  
	<tr>
		<td>
		<input type="submit" class="btn btn-primary" value="Submit">
		</td>
	</tr>
</table>
</form></div> 
<div class="col-md-6">
 <img src="img/employer1.avif" width="100%"/>
</div>
</div>
<%
}
else{
	%>
	<h2>Invalid Session...Login again</h2>
	<br>
	<a href="index.jsp">Login</a>
	
		<%
}}
catch(Exception ex)
{
	
}
%>
 
</body>
</html>