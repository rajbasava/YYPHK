<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="mytags"%>
<html>
<head>
<mytags:style />
</head>
<body>
<mytags:menu />
<form:form method="post" action="processImportFile.htm" commandName="importFile" enctype="multipart/form-data">
<table align="center" cellspacing="2">
	<tr>
		<td><form:label path="eventId"><spring:message code="label.eventId"/></form:label></td>
		<td>
            <form:select path="eventId">
                <form:option value="NONE" label="--- Select ---"/>
                <form:options items="${allEvents}" />
            </form:select>
        </td>
    </tr>
    <tr>
        <td>Please select a file to upload :<form:input type="file" path="file"/></td>
	</tr>
    <tr>
        <td><input type="submit" value="Upload"/></td>
	</tr>
</table>
</form:form>
<mytags:footer/>
</body>
</html>
