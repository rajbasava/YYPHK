<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags"%>

<html>
<head>
	<title>Events - Yoga Vidya Pranic Healing Foundation of Karnataka</title>
	<script type="text/javascript" src="<c:url value="/resources/script/jquery-1.7.2.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/script/jquery-ui-1.8.23.custom.min.js"/>"></script>
</head>
<mytags:style/>
</head>
<body>
<mytags:menu/>
<h2 align="center">Kits for Event: <c:out value="${event.name}"/></h2>

<form:form method="post" action="allotEventKitsAction.htm" commandName="eventKit">

<table align="center" cellspacing="2">
	<tr>
        <td><form:label path="stock"><spring:message code="label.stock"/></form:label></td>
        <td><form:input path="stock" /></td>
	</tr>
	<tr>
		<td colspan="4" align="center">
		    <form:hidden path="eventId"/>
		    <form:hidden path="id"/>
			<input type="submit" value="<spring:message code="label.submit"/>"/>
		</td>
	</tr>
</table>
</form:form>

<mytags:footer/>
</body>
</html>
