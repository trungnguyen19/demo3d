<%@page import="com.javaworld.memcache.Contact"%>
<html>
<body>
<%
	Contact contact = (Contact)request.getAttribute("contact");
 %>
<form action="<%=request.getContextPath() + "/contact?action=update&contactId="+contact.getContactId() %>" method="post">
<table>
	<tr>
		<td>First Name</td>
		<td><input type="text" name="firstName" value='<%=contact.getFirstName() %>'/></td>
	</tr>
	<tr>
		<td>Last Name</td>
		<td><input type="text" name="lastName" value='<%=contact.getLastName() %>'/></td>
	</tr>
	<tr>
		<td>Email</td>
		<td><input type="text" name="email" value='<%=contact.getEmail() %>'/></td>
	</tr>

</table>
</form>
</body>
<a href='<%=request.getContextPath() + "/contact"%>'>Go back to Contact Summary</a>
</html>
