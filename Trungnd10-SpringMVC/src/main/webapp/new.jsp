<%@page import="com.javaworld.memcache.Contact"%>
<html>
<body>
<h2>Create New Contact</h2>

<form action="<%=request.getContextPath() + "/contact?action=insert" %>" method="post">
<table>
	<tr>
		<td>First Name</td>
		<td><input type="text" name="firstName" /></td>
	</tr>
	<tr>
		<td>Last Name</td>
		<td><input type="text" name="lastName" /></td>
	</tr>
	<tr>
		<td>Email</td>
		<td><input type="text" name="email" /></td>
	</tr>
	<tr>
		<td><input type="submit" name="Save" value="Save"></td>
		<td><input type="submit" name="Cancel" value="Cancel"></td>
	</tr>	
</table>
</form>
</body>
</html>
