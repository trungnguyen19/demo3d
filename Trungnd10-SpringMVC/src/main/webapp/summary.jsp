<%@page import="com.javaworld.memcache.Contact"%>
<%@page import="java.util.List"%>
<html>
<body>
<h2>Contact Summary</h2>
<table border='1' bordercolor="black">
<thead>
<tr>
<td>ContactId</td>
<td>First Name</td>
<td>Last Name</td>
<td>EMail</td>
</tr>
</thead>
<%
	List<Contact> contactList =(List<Contact>) request.getAttribute("contactList");
	for(int i = 0 ; i < contactList.size(); i++){
	Contact contact = contactList.get(i);
 %>
 <tr>
 	<td><a href='<%=request.getContextPath() + "/contact?action=select&contactId="+contact.getContactId() %>'><%=contact.getContactId() %></a></td>
 	<td><%=contact.getFirstName() %></td>
 	<td><%=contact.getLastName() %></td>
 	<td><%=contact.getEmail() %></td>
 </tr>
 <%
 }
  %>
  </table>
</body>
</html>
