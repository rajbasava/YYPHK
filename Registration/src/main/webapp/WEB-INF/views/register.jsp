<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="mytags"%>

<html>
<head>
	<title>Arhatic Yoga Retreat - Registration</title>
	<script type="text/javascript" src="<c:url value="/resources/script/jquery-1.7.2.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/script/jquery-ui-1.8.23.custom.min.js"/>"></script>
    <script type="text/javascript">
        function getEventFees(){
            if ($("select#eventId").val() == 'NONE'){
               $("#registeredParticipant input[name='registration.amountPayable']").val('0');
               $("select#eventFeeId").html('<option value="NONE"> --- Select --- </option>');
            }
            else {
                $.getJSON(
                    "getAllEventFees.htm",
                    {eventId: $("select#eventId").val()},
                    function(data) {
                        var options = '<option value="-1"> --- Select --- </option>';
                        var len = data.length;
                        for(var i=0; i<len; i++){
                            options +=  '<option value="' + data[i].id + '">' + data[i].value + '</option>';
                        }
                    $("select#eventFeeId").html(options);
                    $("#registeredParticipant input[name='registration.amountPayable']").val('0');
                    }
                );
            }
        }

        function populateEventFee(){
            if ($("select#eventFeeId").val() == '-1'){
               $("#registeredParticipant input[name='registration.amountPayable']").val('0');
            }
            else {
                $.getJSON(
                    "fetchEventFee.htm",
                    {eventFeeId: $("select#eventFeeId").val()},
                    function(data) {
                        $("#registeredParticipant input[name='registration.amountPayable']").val(data.value);
                    }
                );
            }
        }

        $(document).ready(function() {
            $("select#eventId").change(function()
            {
                getEventFees();
            });

            $("select#eventFeeId").change(function()
            {
                populateEventFee();
            });

            $("#registeredParticipant input[name='currentPayment.receiptDate']").datepicker({
                showOn: 'button',
                dateFormat: 'dd/mm/yy',
                buttonImageOnly: true,
                buttonImage: '<c:url value="/resources/img/calendar.gif"/>'
            });

        });
    </script>
</head>
<mytags:style/>
<body>
<mytags:menu/>
<h2 align="center">User Registration</h2>

<form:form method="post" action="addRegistration.htm" commandName="registeredParticipant">

<table align="center" cellspacing="2">
    <form:hidden path="participant.id" />
    <form:hidden path="registration.id" />
    <tr>
		<td><form:label path="participant.name"><spring:message code="label.name"/></form:label></td>
		<td><form:input path="participant.name" size="50"/></td>
		<td><form:label path="participant.mobile"><spring:message code="label.mobile"/></form:label></td>
		<td><form:input path="participant.mobile" /></td>
	</tr>
	<tr>
		<td><form:label path="participant.email"><spring:message code="label.email"/></form:label></td>
		<td><form:input path="participant.email" /></td>
		<td><form:label path="participant.home"><spring:message code="label.home"/></form:label></td>
		<td><form:input path="participant.home" /></td>
	</tr>
	<tr>
		<td><form:label path="participant.foundation"><spring:message code="label.foundation"/></form:label></td>
        <td>
            <form:select path="participant.foundation">
                <form:option value="" label="--- Select ---"/>
                <form:options items="${allFoundations}" />
            </form:select>
        </td>
    </tr>
	<tr>
		<td><form:label path="participant.vip"><spring:message code="label.vip"/></form:label></td>
		<td><form:checkbox path="participant.vip"/></td>
		<td><form:label path="participant.vipDesc"><spring:message code="label.vipDesc"/></form:label></td>
		<td><form:input path="participant.vipDesc"/></td>
	</tr>
	<tr>
		<td><form:label path="eventId"><spring:message code="label.eventId"/></form:label></td>
		<td>
            <form:select path="eventId">
                <form:option value="NONE" label="--- Select ---"/>
                <form:options items="${allEvents}" />
            </form:select>
        </td>
        <td><form:label path="registration.review"><spring:message code="label.review"/></form:label></td>
        <td><form:checkbox path="registration.review"/></td>
	</tr>
	<tr>
		<td><form:label path="registration.level"><spring:message code="label.level"/></form:label></td>
		<td>
            <form:select path="registration.level">
                <form:option value="NONE" label="--- Select ---"/>
                <form:options items="${allParticipantLevels}" />
            </form:select>
        </td>
		<td><form:label path="eventFeeId"><spring:message code="label.eventFeeId"/></form:label></td>
		<td>
            <form:select path="eventFeeId">
                <form:option value="-1" label="--- Select ---"/>
                <form:options items="${allEventFees}" itemValue="id" itemLabel="value"/>
            </form:select>
        </td>
	</tr>
    <tr>
        <td><form:label path="registration.amountPayable"><spring:message code="label.amountPayable"/></form:label></td>
        <td><form:input path="registration.amountPayable"/></td>
        <td><form:label path="currentPayment.amountPaid"><spring:message code="label.amountPaid"/></form:label></td>
        <td><form:input path="currentPayment.amountPaid"/></td>
    </tr>
    <tr>
        <td><form:label path="currentPayment.receiptInfo"><spring:message code="label.receiptInfo"/></form:label></td>
        <td><form:input path="currentPayment.receiptInfo"/></td>
        <td><form:label path="currentPayment.mode"><spring:message code="label.mode"/></form:label></td>
		<td>
            <form:select path="currentPayment.mode">
                <form:option value="NONE" label="--- Select ---"/>
                <form:options items="${allPaymentModes}" />
            </form:select>
        </td>
    </tr>
    <tr>
        <td><form:label path="currentPayment.receiptDate"><spring:message code="label.receiptDate"/></form:label></td>
        <td><form:input path="currentPayment.receiptDate"/></td>
        <td><form:label path="currentPayment.remarks"><spring:message code="label.remarks"/></form:label></td>
        <td><form:textarea path="currentPayment.remarks" rows="3" cols="30"/></td>
    </tr>
    <tr>
        <td><form:label path="registration.foodCoupon"><spring:message code="label.foodCoupon"/></form:label></td>
        <td><form:checkbox path="registration.foodCoupon"/></td>
        <td><form:label path="registration.eventKit"><spring:message code="label.eventKit"/></form:label></td>
        <td><form:checkbox path="registration.eventKit"/></td>
    </tr>
    <tr>
        <td><form:label path="registration.application"><spring:message code="label.application"/></form:label></td>
        <td><form:checkbox path="registration.application"/></td>
        <td><form:label path="registration.certificates"><spring:message code="label.certificates"/></form:label></td>
        <td><form:checkbox path="registration.certificates"/></td>
    </tr>
    <tr>
        <td><form:label path="currentSeat.seat"><spring:message code="label.seat"/></form:label></td>
        <td><form:input path="currentSeat.seat"/></td>
        <td><form:label path="currentHistoryRecord.comment"><spring:message code="label.comments"/></form:label></td>
        <td><form:textarea path="currentHistoryRecord.comment" rows="3" cols="30"/></td>
    </tr>
    <tr><td>&nbsp;<BR></td></tr>
	<tr colspan="4" align="center">
		<td align="right">
            <form:hidden path="action"/>
			<input type="submit" value="<c:out value="${registeredParticipant.action}"/>"/>
		</td>
		<td align="left">
			<input type="reset" value="Cancel"/>
		</td>
	</tr>
    <tr><td>&nbsp;<BR></td></tr>
    <c:if  test="${!empty registeredParticipant.registration.historyRecords}">
        <tr align="left">
            <td>
                <b>Comments:</b>
            </td>
        </tr>
        <tr>
            <table class="data" border="1" cellpadding="1" cellspacing="1" width="100%">
            <tr>
                <td>Prepared By</td>
                <td>Time Created</td>
                <td>Comment</td>
            </tr>
            <c:forEach items="${registeredParticipant.registration.historyRecords}" var="historyRecord">
                <tr>
                    <td><c:out value="${historyRecord.preparedBy}"/> </td>
                    <td><c:out value="${historyRecord.timeCreated}"/></td>
                    <td><c:out value="${historyRecord.comment}"/></td>
                </tr>
            </c:forEach>
            </table>
        </tr>
    </c:if>
</table>
</form:form>
<mytags:footer/>
</body>
</html>
