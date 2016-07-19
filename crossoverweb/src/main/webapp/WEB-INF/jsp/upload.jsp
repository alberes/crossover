<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="css/crossover.css">
	<title>client-server cloud application that compile Java applications</title>
</head>
<body>
	<div id="container">
		<div id="content">
			<fieldset>
				<legend>Upload</legend>
				<form action="controller?cmd=uploadAction" method="post" enctype="multipart/form-data">
					<input type="file" name="fileToUpload">
					<input type="submit" value="Upload">
				</form>
			</fieldset>
		</div>
		<br />
		<c:forEach items="${requestScope.artifactList}" var="artifact">	
			<div id="panel">
				<c:if test="${artifact.status == 0}">
					<a href="controller?cmd=downloadAction&id=${artifact.id}">
				</c:if>
						<div>
							<p class="textshadow">
								<span>
									<c:choose>
								      <c:when test="${artifact.status == 0}">
										<img src="img/success.png" width="20" height="20" alt="success">
									  </c:when>
									  <c:otherwise>
									  	<img src="img/failure.png" width="20" height="20" alt="failure">
									  </c:otherwise>
									 </c:choose>
									<img src="img/<c:out value="${artifact.projectType}"/>.png" width="20" height="20" alt="${artifact.projectType}">
									Id: <c:out value="${artifact.id}"/> <c:out value="${artifact.projectType}"/>
								</span>
								<span>Started: <fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${artifact.startDate}"/></span>
								<span>Finished: <fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${artifact.finishDate}"/></span>
								<span>Name: <c:out value="${artifact.file}"/></span>
							</p>
						</div>
				<c:if test="${artifact.status == 0}">
					</a>
				</c:if>
				<div class="console">
					<c:out value="${artifact.message}"/>
				</div>
			</div>
		</c:forEach>
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