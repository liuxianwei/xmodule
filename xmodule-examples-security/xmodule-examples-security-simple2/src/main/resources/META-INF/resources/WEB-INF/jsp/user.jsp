<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User</title>
</head>
<body>
	<h1>User: ${sessionScope.loginUser.username}!</h1>
	<div>
		<a href="${pageContext.request.contextPath}/logout">Sign Out</a>
	</div>
</body>
</html>