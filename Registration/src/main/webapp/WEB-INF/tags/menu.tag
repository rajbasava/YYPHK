<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>" />
<script type="text/javascript" src="<c:url value="/resources/script/jquery-1.7.2.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/script/jquery-ui-1.8.23.custom.min.js"/>"></script>
<style>
	TABLE.Table
	{
		 Border:1px Groove;
	}
	TR.Header
	{
		Font-Size:24pt;
		background-color:MidnightBlue;
		Color: LightSteelBlue;
		text-shadow: 2px 2px SlateBlue;
	}
	TR.Caption
	{
		backGround-Color:SlateGray;
		Color:Ivory;
	}
	TR.AlternateRow
	{
		background-color:#E1E1E1;
	}
	TH.ManuPad
	{
		Color:Black;
		BackGround-Color:#E9DCEC;
		font-size:12pt;
	}
	a:link    
	{
		color:Blue;
	}
	a:visited 
	{
		color:Blue;
	}
	a:hover   
	{
		color:red;
	}
	a:active  
	{
		color:yellow;
	}
	#addParticipant
	{
		border:1px Solid Black;
		background-color:black;
		color:Black;
	}
	.Select
	{
		width:98%;
	}
	.Input
	{
		width:98%;
		border:1ps Solid Black;
	}
</style>

<div class="Link">
    <table align="center" border="0" class="YLink" cellPadding="0" cellSpacing="0" width="100%">
        <tr>
            <th width="10%"><img src="<c:url value="/resources/img/gmcks.png"/>"/></th>
		   <th width="80%"><img src="<c:url value="/resources/img/New-Pranic-Logo.png"/>" height="193"/></th>
		   <th width="10%"> Welcome <c:out value="${user.name}"/></th>
        </tr>   
		<tr height="1%"><td></td></tr>
		<tr>
		 <th colspan="3">
                <table align="center" border="0" cellPadding="0" cellSpacing="2" width="100%">
                    <tr>
                        <th width="80px" class="ManuPad"><a href="welcome.htm">Home</a></th>
                        <c:if test="${user.access.admin}" >
                           <th width="125px" class="ManuPad"  ><a href="searchParticipants.htm">Search Participants</a></th>
                        </c:if>
                       <th width="125px" class="ManuPad"  ><a href="search.htm">Search Registrations</a></th>
                        <c:if test="${user.access.spotRegVolunteer || user.access.admin}" >
                           <th width="125px" class="ManuPad"  ><a href="register.htm">Spot Registration</a></th>
                        </c:if>
                        <c:if test="${user.access.admin}" >
                           <th width="125px" class="ManuPad"><a href="volunteer.htm">Manage Volunteers</a></th>
                           <th width="125px" class="ManuPad"><a href="event.htm">Manage Events</a></th>
                           <th width="125px" class="ManuPad"><a href="referenceGroup.htm">Manage&nbsp;Reference&nbsp;Groups</a></th>
                           <th width="125px" class="ManuPad"><a href="import.htm">Import Participants</a></th>
                           <th width="125px" class="ManuPad"><a href="rptRegistrations.htm">Export Registrations</a></th>
                           <th width="125px" class="ManuPad"><a href="rptPayments.htm">Export Payments</a></th>
                        </c:if>
                       <th width="80px" class="ManuPad"  ><a href="logout.htm">Sign Out</a></th>
                    </tr>
                </table>
            </th>
			</tr>		   
    </table>
</div>