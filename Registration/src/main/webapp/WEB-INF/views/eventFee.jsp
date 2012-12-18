<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags"%>

<html>
<head>
	<title>Events - Yoga Vidya Pranic Healing Foundation of Karnataka</title>
	<script type="text/javascript" src="<c:url value="/resources/script/jquery-1.7.2.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/script/jquery-ui-1.8.23.custom.min.js"/>"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            $("#cutOffDate").datepicker({ showOn: 'button', dateFormat: 'dd/mm/yy', buttonImageOnly: true, buttonImage: '<c:url value="/resources/img/calendar.gif"/>' });
        });
    </script>
</head>
<mytags:style/>
</head>
<body>
<mytags:menu/>
<h2 align="center">Event Fees for <c:out value="${event.name}"/></h2>

<form:form method="post" action="addEventFee.htm" commandName="eventFee">

<table align="center" cellspacing="2">
    <tr>
		<td><form:label path="name"><spring:message code="label.name"/></form:label></td>
		<td><form:input path="name" size="30"/></td>
        <td><form:label path="cutOffDate"><spring:message code="label.cutOffDate"/></form:label></td>
        <td><form:input path="cutOffDate"/></td>
	</tr>
	<tr>
        <td><form:label path="amount"><spring:message code="label.amount"/></form:label></td>
        <td><form:input path="amount" /></td>
        <td><form:label path="review"><spring:message code="label.review"/></form:label></td>
        <td><form:checkbox path="review" /></td>
	</tr>
    <tr>
        <td><form:label path="level"><spring:message code="label.level"/></form:label></td>
        <td>
            <form:select path="level">
                <form:option value="NONE" label="--- Select ---"/>
                <form:options items="${allParticipantLevels}" />
            </form:select>
        </td>
	</tr>
	<tr>
		<td colspan="4" align="center">
		    <form:hidden path="eventId"/>
			<input type="submit" value="<spring:message code="label.addEventFee"/>"/>
		</td>
	</tr>
</table>
</form:form>

<c:if  test="${!empty eventFeeList}">
<table class="data" border="1" cellpadding="1" cellspacing="1" width="100%">
<tr>
    <th><spring:message code="label.name"/></th>
    <th><spring:message code="label.cutOffDate"/></th>
    <th><spring:message code="label.amount"/></th>
    <th><spring:message code="label.review"/></th>
	<th>&nbsp;</th>
</tr>
<c:forEach items="${eventFeeList}" var="eventFee">
	<tr>
		<td><c:out value="${eventFee.name}"/> </td>
		<td><c:out value="${eventFee.cutOffDate}"/></td>
		<td><c:out value="${eventFee.amount}"/> </td>
		<td><c:out value="${eventFee.review}"/></td>
		<td class="YLink">
            <form id="delEventFee<c:out value="${eventFee.id}"/>" method="post" action="deleteEventFee.htm">
                <input type="hidden" name="eventFeeId" value="<c:out value="${eventFee.id}"/>" />
                <input type="hidden" name="eventId" value="<c:out value="${event.id}"/>" />
                <a href="#" onclick="document.getElementById('delEventFee<c:out value="${eventFee.id}"/>').submit();"><spring:message code="label.delete"/></a>
            </form>
        </td>
	</tr>
</c:forEach>
</table>
</c:if>

<mytags:footer/>
</body>
</html>
