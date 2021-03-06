<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags"%>

<html>
<head>
	<title>Participants - Yoga Vidya Pranic Healing Foundation of Karnataka</title>
	<mytags:style/>
	<mytags:menu/>
	<script type="text/javascript">
		$(document).ready(function(){
			$(function() {
				$("a#createParticipant").button();
				$("a#createParticipant").css("font-size", "11px");

				$("a#back").button();
				$("a#back").css("font-size", "11px");
			});
		});
	</script>
</head>
<body>
<form:form method="post" action="createParticipant.htm" commandName="newParticipant">
    <table align="center" cellpadding="2" cellspacing="2">
        <tr>
            <td><form:label path="name"><spring:message code="label.name"/></form:label></td>
            <td><form:input path="name" size="50"/></td>
        </tr>
        <tr>
            <td><form:label path="mobile"><spring:message code="label.mobile"/></form:label></td>
            <td><form:input path="mobile" /></td>
        </tr>
        <tr>
            <td><form:label path="home"><spring:message code="label.home"/></form:label></td>
            <td><form:input path="home" /></td>
        </tr>
        <tr>
            <td><form:label path="email"><spring:message code="label.email"/></form:label></td>
            <td><form:input path="email" /></td>
        </tr>
        <tr>
            <td><form:label path="foundation"><spring:message code="label.foundation"/></form:label></td>
            <td>
                <form:select path="foundation">
                    <form:option value="" label="--- Select ---"/>
                    <form:options items="${allFoundations}" />
                </form:select>
            </td>
        </tr>
        <tr>
            <td><form:label path="vip"><spring:message code="label.vip"/></form:label></td>
            <td><form:checkbox path="vip"/></td>
        </tr>
        <tr>
            <td><form:label path="vipDesc"><spring:message code="label.vipDesc"/></form:label></td>
            <td><form:input path="vipDesc"/></td>
        </tr>
        <tr align="center">
            <td colspan ="2">
                <table width="100%" cellpadding="2" cellspacing="2">
                    <tr>
                        <td align="right">
                            <a id="createParticipant" onclick="document.getElementById('newParticipant').submit();">Create Participant</a>
                        </td>
                        <td align="left">
                            <a id="back" href="searchParticipants.htm">Back</a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</form:form>
<mytags:footer/>
</body>
</html>