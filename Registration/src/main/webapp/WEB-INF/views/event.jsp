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
            $("#startDate").datepicker({ showOn: 'button', dateFormat: 'dd/mm/yy', buttonImageOnly: true, buttonImage: '<c:url value="/resources/img/calendar.gif"/>' });
            $("#endDate").datepicker({ showOn: 'button', dateFormat: 'dd/mm/yy', buttonImageOnly: true, buttonImage: '<c:url value="/resources/img/calendar.gif"/>' });
        });
    </script>
</head>
<mytags:style/>
<body>
<mytags:menu/>
<h2 align="center">Events</h2>

<form:form method="post" action="addEvent.htm" commandName="event">

<table align="center" cellspacing="2">
    <tr>
		<td><form:label path="name"><spring:message code="label.name"/></form:label></td>
		<td><form:input path="name" size="30"/></td>
        <td><form:label path="eligibilityLevel"><spring:message code="label.eligibilityLevel"/></form:label></td>
        <td>
            <form:select path="eligibilityLevel">
                <form:option value="NONE" label="--- Select ---"/>
                <form:options items="${allParticipantLevels}" />
            </form:select>
        </td>
	</tr>
	<tr>
		<td><form:label path="venue"><spring:message code="label.venue"/></form:label></td>
		<td><form:input path="venue"/></td>
		<td><form:label path="startDate"><spring:message code="label.startDate"/></form:label></td>
        <td><form:input path="startDate" /></td>
	</tr>
	<tr>
		<td><form:label path="seatPerLevel"><spring:message code="label.seatPerLevel"/></form:label></td>
        <td><form:checkbox path="seatPerLevel" /></td>
		<td><form:label path="endDate"><spring:message code="label.endDate"/></form:label></td>
        <td><form:input path="endDate" /></td>
	</tr>
	<tr>
		<td><form:label path="city"><spring:message code="label.city"/></form:label></td>
        <td><form:input path="city" /></td>
		<td><form:label path="state"><spring:message code="label.state"/></form:label></td>
        <td><form:input path="state" /></td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input type="submit" value="<spring:message code="label.addEvent"/>"/>
		</td>
	</tr>
</table>
</form:form>

<c:if  test="${!empty eventList}">
<table class="data" border="1" cellpadding="1" cellspacing="1" width="100%">
<tr>
    <th><spring:message code="label.name"/></th>
    <th><spring:message code="label.venue"/></th>
    <th><spring:message code="label.city"/></th>
    <th><spring:message code="label.eligibilityLevel"/></th>
    <th><spring:message code="label.startDate"/></th>
    <th><spring:message code="label.endDate"/></th>
    <th><spring:message code="label.seatPerLevel"/></th>
	<th>&nbsp;</th>
</tr>
<c:forEach items="${eventList}" var="event">
	<tr>
	    <td>
            <form id="addDisc<c:out value="${event.id}"/>" method="post" action="eventFee.htm">
                <input type="hidden" name="eventId" value="<c:out value="${event.id}"/>" />
                <a href="#" onclick="document.getElementById('addDisc<c:out value="${event.id}"/>').submit();"><c:out value="${event.name}"/></a>
            </form>
        </td>
		<td><c:out value="${event.venue}"/></td>
		<td><c:out value="${event.city}"/> </td>
		<td><c:out value="${event.eligibilityLevel}"/></td>
		<td><c:out value="${event.startDate}"/> </td>
		<td><c:out value="${event.endDate}"/></td>
		<td><c:out value="${event.seatPerLevel}"/></td>
		<td>
            <form id="delEvent<c:out value="${event.id}"/>" method="post" action="deleteEvent.htm">
                <input type="hidden" name="eventId" value="<c:out value="${event.id}"/>" />
                <a href="#" onclick="document.getElementById('delEvent<c:out value="${event.id}"/>').submit();"><spring:message code="label.delete"/></a>
            </form>
        </td>
	</tr>
</c:forEach>
</table>
</c:if>

<mytags:footer/>
</body>
</html>
