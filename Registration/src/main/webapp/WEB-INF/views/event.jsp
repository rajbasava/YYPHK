<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags"%>

<html>
<head>
	<title>Events - Yoga Vidya Pranic Healing Foundation of Karnataka</title>
	<mytags:style/>
    <mytags:menu/>
    <script type="text/javascript">
        $(document).ready(function(){
            $("#startDate").datepicker({ showOn: 'button', dateFormat: 'dd/mm/yy', buttonImageOnly: true, buttonImage: '<c:url value="/resources/img/calendar.gif"/>' });
            $("#endDate").datepicker({ showOn: 'button', dateFormat: 'dd/mm/yy', buttonImageOnly: true, buttonImage: '<c:url value="/resources/img/calendar.gif"/>' });

            $("a#allocateSeats").button();
            $("a#allocateSeats").css("font-size", "11px");
            $("a#allocateSeats").click(function() {
                 $("#modifyEvent").get(0).setAttribute('action', 'allocateSeats.htm');
                 $("#modifyEvent").submit();
            });

            $("a#addEventFee").button();
            $("a#addEventFee").css("font-size", "11px");
            $("a#addEventFee").click(function() {
                 $("#modifyEvent").get(0).setAttribute('action', 'eventFee.htm');
                 $("#modifyEvent").submit();
            });

            $("a#showKits").button();
            $("a#showKits").css("font-size", "11px");
            $("a#showKits").click(function() {
                 $("#modifyEvent").get(0).setAttribute('action', 'showKitsUI.htm');
                 $("#modifyEvent").submit();
            });

            $("a#showEventDetail").button();
            $("a#showEventDetail").css("font-size", "11px");
            $("a#showEventDetail").click(function() {
                 $("#modifyEvent").get(0).setAttribute('action', 'showEventDetailUI.htm');
                 $("#modifyEvent").submit();
            });

            $("a#deleteEvent").button();
            $("a#deleteEvent").css("font-size", "11px");
            $("a#deleteEvent").click(function() {
                 $("#modifyEvent").get(0).setAttribute('action', 'deleteEvent.htm');
                 $("#modifyEvent").submit();
            });

            $("a#exportSeats").button();
            $("a#exportSeats").css("font-size", "11px");
            $("a#exportSeats").click(function() {
                 $("#modifyEvent").get(0).setAttribute('action', 'exportSeats.htm');
                 $("#modifyEvent").submit();
            });

            $("a#editEvent").button();
            $("a#editEvent").css("font-size", "11px");
            $("a#editEvent").click(function() {
                 $("#modifyEvent").get(0).setAttribute('action', 'showUpdateEvent.htm');
                 $("#modifyEvent").submit();
            });

        });
    </script>
    <style>
	.error {
		color: #ff0000;
	}
 
	.errorblock {
		color: #000;
		background-color: #ffEEEE;
		border: 3px solid #ff0000;
		padding: 8px;
		margin: 16px;
	}
</style>
</head>
<body>
<table width="100%" cellpadding="2" cellspacing="2">
    <tr>
        <td align="center" style="font-size:18px">
            Manage Events
        </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
</table>
<form:form method="post" action="saveOrUpdateEvent.htm" commandName="event">
<form:errors path="*" cssClass="errorblock" element="div" />
<form:hidden path="id"/>
<form:hidden path="seatAllocated"/>
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
	    <td><form:label path="seatingType"><spring:message code="label.seatingType"/></form:label></td>
        <td>
            <form:select path="seatingType">
                <form:option value="NONE" label="--- Select ---"/>
                <form:options items="${allSeatingTypes}" />
            </form:select>
        </td>
        <td><form:label path="rowMetaName"><spring:message code="label.rowMetaName"/></form:label></td>
        <td>
            <form:select path="rowMetaName">
                <form:option value="NONE" label="--- Select ---"/>
                <form:options items="${allRowMetaNames}" />
            </form:select>
        </td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input type="submit" value="Save Or Update Event"/>
		</td>
	</tr>
</table>
</form:form>

<c:if  test="${!empty eventList}">
<table width="100%" cellpadding="3" cellspacing="3">
    <tr style="background-color:#E8E8E8;">
        <td>Events</td>
    </tr>
    <tr>
        <td>
            <table style="width:100%; table-layout:fixed;">
                <tr>
                    <td>
                        <table border="1" width="100%">
                            <tr>
                                <td width="3%">&nbsp;&nbsp;</td>
                                <td width="15%"><spring:message code="label.name"/></td>
                                <td width="10%"><spring:message code="label.venue"/></td>
                                <td width="8%"><spring:message code="label.city"/></td>
                                <td width="10%"><spring:message code="label.eligibilityLevel"/></td>
                                <td width="12%"><spring:message code="label.startDate"/></td>
                                <td width="12%"><spring:message code="label.endDate"/></td>
                                <td width="8%"><spring:message code="label.seatingType"/></td>
                                <td width="8%"><spring:message code="label.rowMetaName"/></td>
                                <td><spring:message code="label.seatAllocated"/></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div style="width:100%; height:150px; overflow-y:scroll; overflow-x:hidden; empty-cells:show; font-size:11px">
                            <table border="1" width="100%">
                                <form id="modifyEvent" method="post" action="">
                                    <c:forEach items="${eventList}" var="event">
                                        <tr>
                                            <td width="3%"><input type="radio" name="eventId" value="<c:out value="${event.id}"/>"></td>
                                            <td width="15%"><c:out value="${event.name}"/></td>
                                            <td width="10%"><c:out value="${event.venue}"/></td>
                                            <td width="8%"><c:out value="${event.city}"/> </td>
                                            <td width="10%"><c:out value="${event.eligibilityLevel}"/></td>
                                            <td width="12%"><c:out value="${event.startDate}"/> </td>
                                            <td width="12%"><c:out value="${event.endDate}"/></td>
                                            <td width="8%"><c:out value="${event.seatingTypeName}"/></td>
                                            <td width="8%"><c:out value="${event.rowMetaName}"/></td>
                                            <td><c:out value="${event.seatAllocated}"/></td>
                                        </tr>
                                    </c:forEach>
                                </form>
                            </table>
                        </div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr style="background-color:#E8E8E8;">
        <td>
            <table width="100%" cellpadding="2" cellspacing="2">
                <tr>
                    <td align="left">
                        <a id="addEventFee" href="#">Manage Event Fees</a>
                        <a id="showKits" href="#">Volunteer Kits and Status</a>
                        <a id="showEventDetail" href="#">Kits For Event</a>
                        <a id="allocateSeats" href="#">Allocate Seats</a>
                        <a id="exportSeats" href="#">Export Allocated Seats</a>
                        <a id="deleteEvent" href="#">Deactivate</a>
                        <a id="editEvent" href="#">Edit</a>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</c:if>

<mytags:footer/>
</body>
</html>
