<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags"%>

<html>
<head>
    <title>Arhatic Yoga Retreat - Search Registrations</title>
    <script type="text/javascript" src="<c:url value="/resources/script/jquery-1.7.2.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/resources/script/jquery-ui-1.8.23.custom.min.js"/>"></script>
    <script type="text/javascript">
        $(function() {
            var moveLeft = 250;
            var moveDown = 200;
            $("a#submit").button();
            $("a#submit").css("font-size", "11px");
            $( "a#submit" ).click(function() {
                 $("#registrationCriteria").get(0).setAttribute('action', 'list.htm');
                 $("#registrationCriteria").submit();
            });

            $(document).ready(function() {

                $("#registrationCriteria input[name='fromRegistrationDate']").datepicker({
                    showOn: 'button',
                    dateFormat: 'dd/mm/yy',
                    buttonImageOnly: true,
                    buttonImage: '<c:url value="/resources/img/calendar.gif"/>'
                });

                $("#registrationCriteria input[name='toRegistrationDate']").datepicker({
                    showOn: 'button',
                    dateFormat: 'dd/mm/yy',
                    buttonImageOnly: true,
                    buttonImage: '<c:url value="/resources/img/calendar.gif"/>'
                });

            });
        });
    </script>
</head>
<mytags:style/>
</head>
<body>
<mytags:menu/>
<table width="100%" cellpadding="1" cellspacing="1">
    <tr>
        <td align="center" style="font-size:18px">
            Search Registrations
        </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
</table>
<form:form method="post" action="list.htm" commandName="registrationCriteria">
<table align="center" cellspacing="3" cellpadding="3">
    <tr>
        <td><form:label path="name"><spring:message code="label.name"/></form:label></td>
        <td><form:input path="name" /></td>
        <td><form:label path="foundation"><spring:message code="label.foundation"/></form:label></td>
        <td>
            <form:select path="foundation">
                <form:option value="" label="--- Select ---"/>
                <form:options items="${allFoundations}" />
            </form:select>
        </td>
    </tr>
    <tr>
        <td><form:label path="email"><spring:message code="label.email"/></form:label></td>
        <td><form:input path="email" /></td>
        <td><form:label path="level"><spring:message code="label.level"/></form:label></td>
        <td>
            <form:select path="level">
                <form:option value="" label="--- Select ---"/>
                <form:options items="${allParticipantLevels}" />
            </form:select>
        </td>
    </tr>
    <tr>
        <td><form:label path="mobile"><spring:message code="label.mobile"/></form:label></td>
        <td><form:input path="mobile" /></td>
        <td><form:label path="seat"><spring:message code="label.seat"/></form:label></td>
        <td><form:input path="seat" /></td>
    </tr>
    <tr>
        <td><form:label path="eventId"><spring:message code="label.eventId"/></form:label></td>
        <td>
            <form:select path="eventId">
                <form:option value="" label="--- Select ---"/>
                <form:options items="${allEvents}" />
            </form:select>
        </td>
        <td><form:label path="vip"><spring:message code="label.vip"/></form:label></td>
        <td><form:checkbox path="vip"/></td>
    </tr>
    <tr>
        <td><form:label path="foodCoupon"><spring:message code="label.foodCoupon"/></form:label></td>
        <td>
            <form:radiobutton path="foodCoupon" value="true"/>True &nbsp;
            <form:radiobutton path="foodCoupon" value="false"/>False &nbsp;
            <form:radiobutton path="foodCoupon" value=""/>Both
        </td>
        <td><form:label path="eventKit"><spring:message code="label.eventKit"/></form:label></td>
        <td>
            <form:radiobutton path="eventKit" value="true"/>True &nbsp;
            <form:radiobutton path="eventKit" value="false"/>False &nbsp;
            <form:radiobutton path="eventKit" value=""/>Both
        </td>
    </tr>
    <tr>
        <td><spring:message code="label.registrationDate"/></td>
        <td>
            <table width="100%">
                <tr>
                    <td>From: </td><td><form:input path="fromRegistrationDate"/></td>
                </tr>
                <tr>
                    <td>To: </td><td><form:input path="toRegistrationDate"/></td>
                </tr>
            </table>
        </td>
        <td>
            <table width="100%" cellspacing="2" cellpadding="2">
                <tr>
                    <td><form:label path="status"><spring:message code="label.status"/></form:label></td>
                    <td>
                        <form:select path="status">
                            <form:option value="" label="--- Select ---"/>
                            <form:options items="${allStatuses}" />
                        </form:select>
                    </td>
                </tr>
                <tr>
                    <td><form:label path="reference"><spring:message code="label.reference"/></form:label></td>
                    <td>
                        <form:select path="reference">
                            <form:option value="" label="--- Select ---"/>
                            <form:options items="${allReferenceGroups}" />
                        </form:select>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="4" align="center">
            <div id="button">
                <a id="submit" href="#"><spring:message code="label.search"/></a>
            </div>
        </td>
    </tr>
</table>
</form:form>

<c:if  test="${!empty registrationList}">
<c:set var="registrationListSize" value="${fn:length(registrationList)}"/>
<table width="100%" cellpadding="2" cellspacing="2">
    <tr style="background-color:#E8E8E8;">
        <td>Search found <c:out value="${registrationListSize}"/> items.</td>
    </tr>
    <tr>
        <td>
            <table style="width:100%; table-layout:fixed;">
                <tr>
                    <td>
                        <table border="1" width="100%">
                            <tr>
                                <td width="3%"><spring:message code="label.id"/></th>
                                <td width="9.75%"><spring:message code="label.name"/></th>
                                <td width="11%"><spring:message code="label.email"/></th>
                                <td width="8%"><spring:message code="label.mobile"/></th>
                                <td width="15%"><spring:message code="label.foundation"/></th>
                                <td width="8.5%"><spring:message code="label.event"/></th>
                                <td width="7.5%"><spring:message code="label.level"/></th>
                                <td width="9%"><spring:message code="label.totalAmountPaid"/></th>
                                <td width="7.5%"><spring:message code="label.amountDue"/></th>
                                <td width="7%"><spring:message code="label.foodCoupon"/></th>
                                <td width="6%"><spring:message code="label.eventKit"/></th>
                                <td><spring:message code="label.status"/></th>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div style="width:100%; height:300px; overflow-y:scroll; overflow-x:hidden; empty-cells:show; font-size:11px">
                            <table border="1" width="100%">
                                <c:forEach items="${registrationList}" var="registration">
                                    <tr>
                                        <td width="3%"><c:out value="${registration.id}"/></td>
                                        <td width="10%" class="YLink">
                                            <form id="updatePart<c:out value="${registration.id}"/>" method="post" action="updateRegistration.htm">
                                                <input type="hidden" name="registrationId" value="<c:out value="${registration.id}"/>" />
                                                <a href="#" onclick="document.getElementById('updatePart<c:out value="${registration.id}"/>').submit();">
                                                    <c:out value="${registration.participant.name}"/>
                                                </a>
                                            </form>
                                        </td>
                                        <td width="11%"><c:out value="${registration.participant.email}"/></td>
                                        <td width="8.25%"><c:out value="${registration.participant.mobile}"/></td>
                                        <td width="15%"><c:out value="${registration.participant.foundation}"/></td>
                                        <td width="8.75%"><c:out value="${registration.event.name}"/></td>
                                        <td width="7.75%"><c:out value="${registration.levelName}"/></td>
                                        <td width="9%">
                                            <c:out value="${registration.totalAmountPaid}"/>
                                        </td>
                                        <td width="7.75%"><c:out value="${registration.amountDue}"/></td>
                                        <td width="7%"><c:out value="${registration.foodCoupon}"/></td>
                                        <c:if  test="${registration.eventKit}">
                                            <td width="5%" style="font-weight:bold; color:green; font-size:20px;">
                                                <c:out value="${registration.eventKit}"/>
                                            </td>
                                        </c:if>
                                        <c:if  test="${!registration.eventKit}">
                                            <td width="6%">
                                                <c:out value="${registration.eventKit}"/>
                                            </td>
                                        </c:if>
                                        <td><c:out value="${registration.status}"/></td>
                                    </tr>
                                </c:forEach>
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
                    <td>
                        &nbsp;
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
