<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags"%>
<html>
<head>
<mytags:style />
</head>
<body>
<mytags:menu />
<c:out value="${results}"/>
<mytags:footer/>
</body>
</html>
