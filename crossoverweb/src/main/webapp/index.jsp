<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="css/crossover.css">
	<title>client-server cloud application that compile Java applications</title>
</head>
<body>
<body>
	<div id="container">
		<h2>client-server cloud application that compile Java applications</h2>
		<div id="content">
			<fieldset>
				<legend>Login</legend>
				<form action="controller?cmd=logginAction" method="post">
					<input type="text" label="Name" name="name" value=""/>
					<input type="password" label="Password" name="password" value=""/>
					<input type="submit" value="Submit"/>
				</form>
			</fieldset>
		</div>
		<br />
		<div>
			<table>
				<c:forEach items="${requestScope.messagesError}" var="message">
		        <tr>
		          <td><c:out value='${message}'/></td>
		        </tr>
		      </c:forEach>
	      </table>
		</div>
	</div>
</body>
</html>
