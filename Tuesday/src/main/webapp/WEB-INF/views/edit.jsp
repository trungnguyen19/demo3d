<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
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
	<h1>Hello world!</h1>

	<div align="center">
		<h2 style="color: #8e9182">Demo</h2>
	</div>

	<form name="data_entry" id="data_entry_frm" action="rest/hello/xxxxx">
		<table cellspacing="5" cellpadding="0">
			<tr>
				<td><label for="company_name">Product Code (Id):</label></td>
				<td><input type="text" size="35" maxlength="70"
					name="company_name" id="txt_company_name" value="${productId}"></td>
			</tr>
			<tr>
				<td><label for="company_name">Delivery Man Name:</label></td>
				<td><input type="text" size="35" maxlength="70"
					name="company_name" id="txt_company_name"
					value="${deliveryManName}"></td>
			</tr>
			<tr>
				<td><label for="company_name">Telephone Number:</label></td>
				<td><input type="text" size="35" maxlength="70"
					name="company_name" id="txt_company_name" value="${telephoneNum}"></td>
			</tr>
			<tr>
				<td><label for="company_name">Product Name:</label></td>
				<td><input type="text" size="35" maxlength="70"
					name="company_name" id="txt_company_name" value="${productName}"></td>
			</tr>
			<tr>
				<td><label for="company_name">Product Information:</label></td>
                <td><textarea name="ho_address" id="txta_ho_address" rows="3"
                        cols="30">${productInformation}</textarea></td>
			</tr>
			<tr>
				<td><label for="company_name">Product Information:</label></td>
				<td><input type="text" size="35" maxlength="70"
					name="company_name" id="txt_company_name"
					value="${productInformation}"></td>
			</tr>
			<tr>
				<td><label for="company_name">Note:</label></td>
				<td><textarea name="ho_address" id="txta_ho_address" rows="3"
						cols="30">${note}</textarea></td>
			</tr>
			<tr>
				<td><label for="company_name">Receiver Name:</label></td>
				<td><input type="text" size="35" maxlength="70"
					name="company_name" id="txt_company_name" value="${receiverName}"></td>
			</tr>
			<tr>
				<td><label for="company_name">Receiver Date:</label></td>
				<td><input type="text" size="35" maxlength="70"
					name="company_name" id="txt_company_name" value="${receiveDate}"></td>
			</tr>
			<tr>
				<td><label for="company_name">State:</label></td>
                <td><select name="slt_country" id="country">
                        <option value="">- Select -</option>
                        <option value="ZW">Xuat sac</option>
                        <option value="AF" <c:if test="${state  == 'Tot'}">selected="selected"</c:if>>Tot</option>
                        <option value="AL">Trung Binh</option>
                        <option value="DZ">Kem</option>
                </select></td>
			</tr>
			<tr>
				<td><label for="company_name">expiryDate:</label></td>
				<td><input type="text" size="35" maxlength="70"
					name="company_name" id="txt_company_name" value="${expiryDate}"></td>
			</tr>

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
			<tr>
				<td><input type="button" name="clear" value="Clear Form"
					onclick="clearForm(this.form);"></td>
				<td><input type="button" name="reset_form" value="Reset Form"
					onclick="this.form.reset();"></td>
				<td><input type="button" name="submit_form" value="Submit Form"
					onclick="this.form.submit();"></td>
			</tr>
		</table>
	</form>

</body>
</html>
