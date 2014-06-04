<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.net.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.mycompany.store.*"%>
<html>
<head>
<title>Home</title>
<script script type="text/javascript">
	function clearForm(oForm) {

		var elements = oForm.elements;

		oForm.reset();

		for (i = 0; i < elements.length; i++) {

			field_type = elements[i].type.toLowerCase();

			switch (field_type) {

			case "text":
			case "password":
			case "textarea":
			case "hidden":

				elements[i].value = "";
				break;

			case "radio":
			case "checkbox":
				if (elements[i].checked) {
					elements[i].checked = false;
				}
				break;

			case "select-one":
			case "select-multi":
				elements[i].selectedIndex = -1;
				break;

			default:
				break;
			}
		}
	}
</script>
</head>
<body>
	<h1>Edit</h1>
	<!--
	<div align="center">
		<h2 style="color: #8e9182">Demo</h2>
	</div>
	-->
    <%
        URL reconstructedURL = new URL(request.getScheme(),
                request.getServerName(), request.getServerPort(),
                Constants.getSubmitLink());
    String submitLink = reconstructedURL.toString();
        %>

	<form method="POST" name="data_entry" id="data_entry_frm"
		action="<% out.print(submitLink); %>">
		<table cellspacing="5" cellpadding="0">
			<tr>
				<td><label for="product_id">Product Code (Id):</label></td>
				<td><input type="text" size="35" maxlength="70"
					name="product_id" id="txt_product_id" value="${productId}"></td>
			</tr>
			<tr>
				<td><label for="delivery_man_name">Delivery Man Name:</label></td>
				<td><input type="text" size="35" maxlength="70"
					name="delivery_man_name" id="txt_delivery_man_name"
					value="${deliveryManName}"></td>
			</tr>
			<tr>
				<td><label for="telephone_num">Telephone Number:</label></td>
				<td><input type="text" size="35" maxlength="70"
					name="telephone_num" id="txt_telephone_num" value="${telephoneNum}"></td>
			</tr>
			<tr>
				<td><label for="product_name">Product Name:</label></td>
				<td><input type="text" size="35" maxlength="70"
					name="product_name" id="txt_product_name" value="${productName}"></td>
			</tr>
			<tr>
				<td><label for="product_information">Product
						Information:</label></td>
				<td><textarea name="product_information"
						id="txta_product_information" rows="3" cols="30">${productInformation}</textarea></td>
			</tr>
			<tr>
				<td><label for="note">Note:</label></td>
				<td><textarea name="note" id="txta_note" rows="3" cols="30">${note}</textarea></td>
			</tr>
			<tr>
				<td><label for="receiver_name">Receiver Name:</label></td>
				<td><input type="text" size="35" maxlength="70"
					name="receiver_name" id="txt_receiver_name" value="${receiverName}"></td>
			</tr>
			<tr>
				<td><label for="receive_date">Receiver Date:</label></td>
				<td><input type="text" size="35" maxlength="70"
					name="receive_date" id="txt_receive_date" value="${receiveDate}"></td>
			</tr>
			<tr>
				<td><label for="state">State:</label></td>
				<td><select name="state" id="slt_state">
						<option value="">- Select -</option>
						<% String goodValue = Constants.getTranslateProperties("good"); %>
						<option value="<%=goodValue %>"
							<c:if test="${state == goodValue}">selected="selected"</c:if>><%=goodValue %></option>
                        <% String badValue = Constants.getTranslateProperties("bad"); %>
                        <option value="<%=badValue %>"
                            <c:if test="${state == badValue}">selected="selected"</c:if>><%=badValue %></option>
				</select></td>
			</tr>
			<tr>
				<td><label for="expiry_date">expiryDate:</label></td>
				<td><input type="text" size="35" maxlength="70"
					name="expiry_date" id="txt_expiry_date" value="${expiryDate}"></td>
			</tr>

			<!--
			<tr>
				<td><label for="business_category">Select Business
						Type:</label></td>
				<td><input type="radio" name="business_category" value="1"
					id="rad_business_category_1">Manufacturer</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><input type="radio" name="business_category" value="2"
					id="rad_business_category_2">Whole Sale Supplier</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><input type="radio" name="business_category" value="3"
					id="rad_business_category_3" checked>Retailer</label>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><input type="radio" name="business_category" value="4"
					id="rad_business_category_4">Service Provider</td>
			</tr>
			<tr>
				<td><label for="privacy">Keep Information Private: </label></td>
				<td><input type="checkbox" name="privacy" id="chk_privacy"
					checked></td>
			</tr>
			-->
			<tr>
				<td><input type="button" name="clear" value="Clear Form"
					onclick="clearForm(this.form);"> <input type="button"
					name="reset_form" value="Reset Form" onclick="this.form.reset();"></td>
				<td><input type="button" name="submit_form" value="Submit Form"
					onclick="this.form.submit();"></td>
			</tr>
		</table>
	</form>

	<%
		reconstructedURL = new URL(request.getScheme(),
				request.getServerName(), request.getServerPort(),
				Constants.getListLink());
		out.print("<a href=\"" + reconstructedURL.toString()
				+ "\">List</a>");
	%>

</body>
</html>
