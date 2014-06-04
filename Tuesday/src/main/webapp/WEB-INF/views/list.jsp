<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.net.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.mycompany.store.*"%>
<html>
<head>
<title>List</title>
</head>
<body>
	<h1>List</h1>

	<%
		String file = request.getRequestURI();
		if (request.getQueryString() != null) {
			file += '?' + request.getQueryString();
		}
		file = Constants.getEditLink(0);
		URL reconstructedURL = new URL(request.getScheme(),
				request.getServerName(), request.getServerPort(), file);

		List<DeliveryDetail> list = (List<DeliveryDetail>) request
				.getAttribute("list");
	%>

	<table border="1">
		<tr>
			<td>ProductId</td>
			<td>DeliveryManName</td>
			<td>TelephoneNum</td>
			<td>ProductName</td>
			<td>ProductInformation</td>
			<td>Note</td>
			<td>ReceiverName</td>
			<td>ReceiveDate</td>
			<td>State</td>
			<td>ExpiryDate</td>
			<td>Action</td>
		</tr>

		<%
			for (DeliveryDetail dd : list) {
				out.print("<tr>");
				out.print("<td>" + dd.getProductId() + "</td>");
				out.print("<td>" + dd.getDeliveryManName() + "</td>");
				out.print("<td>" + dd.getTelephoneNum() + "</td>");
				out.print("<td>" + dd.getProductName() + "</td>");
				out.print("<td>" + dd.getProductInformation() + "</td>");
				out.print("<td>" + dd.getNote() + "</td>");
				out.print("<td>" + dd.getReceiverName() + "</td>");
				out.print("<td>" + dd.getReceiveDate() + "</td>");
				out.print("<td>" + dd.getState() + "</td>");
				out.print("<td>" + dd.getExpiryDate() + "</td>");
				reconstructedURL = new URL(request.getScheme(),
						request.getServerName(), request.getServerPort(),
						Constants.getEditLink(dd.getProductId()));
				out.print("<td><a href=" + reconstructedURL.toString()
						+ ">edit</a></td>");
				out.print("</tr>");
			}
		%>

		<!--		
		<c:forEach items="${list}" var="item">
			<tr>
				<td><c:out value="${item.productId}" /></td>
				<td><c:out value="${item.deliveryManName}" /></td>
				<td><c:out value="${item.telephoneNum}" /></td>
				<td><c:out value="${item.productName}" /></td>
				<td><c:out value="${item.productInformation}" /></td>
				<td><c:out value="${item.note}" /></td>
				<td><c:out value="${item.receiverName}" /></td>
				<td><c:out value="${item.receiveDate}" /></td>
				<td><c:out value="${item.state}" /></td>
				<td><c:out value="${item.expiryDate}" /></td>
				<td><a href="">edit</a></td>
			</tr>
		</c:forEach>
		-->
	</table>
</body>
</html>
