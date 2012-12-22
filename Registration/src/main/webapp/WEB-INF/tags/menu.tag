<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>" type
<script type="text/javascript" src="<c:url value="/resources/script/jquery-1.7.2.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/script/jquery-ui-1.8.23.custom.min.js"/>"></script>

<div class="YLink">
    <table align="center">
        <tr>
            <td>
                <img src="<c:url value="/resources/img/gmcks.png"/>"/>
            </td>
            <td>
                <table align="center" cellspacing="1" cellpadding="1">
                    <tr>
                        <th colspan="11">Yoga Vidya Pranic Healing Foundation of Karnataka </th>
                    </tr>
                    <tr>
                        <th colspan="11">Arhatic Yoga Retreat - 2013 </th>
                    </tr>
                    <tr>
                        <td width="80px"><a href="welcome.htm">Home</a></td>
                        <c:if test="${user.access.admin}" >
                            <td width="125px"><a href="searchParticipants.htm">Search Participants</a></td>
                        </c:if>
                        <td width="125px"><a href="search.htm">Search Registrations</a></td>
                        <c:if test="${user.access.spotRegVolunteer || user.access.admin}" >
                            <td width="125px"><a href="register.htm">Spot Registration</a></td>
                        </c:if>
                        <c:if test="${user.access.admin}" >
                            <td width="125px"><a href="volunteer.htm">Manage Volunteers</a></td>
                            <td width="125px"><a href="event.htm">Manage Events</a></td>
                            <td width="125px"><a href="referenceGroup.htm">Manage Reference Groups</a></td>
                            <td width="125px"><a href="import.htm">Import Participants</a></td>
                            <td width="125px"><a href="rptRegistrations.htm">Export Registrations</a></td>
                            <td width="125px"><a href="rptPayments.htm">Export Payments</a></td>
                        </c:if>
                        <td width="80px"><a href="logout.htm">Sign Out</a></td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
    </table>
</div>