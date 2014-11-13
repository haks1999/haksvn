<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<div id="leftmenu">
	<div class="menu_list">
		<div class="content">
			<div class="col w10">
				<c:forEach items="${leftMenuList}" var="leftMenu" varStatus="loop">
					<a href="<c:url value="${leftMenu.menuUrl}"/>"> <c:out value="${fn:replace(leftMenu.menuName,'_',' ')}" /> </a>
					<hr />
				</c:forEach>
			</div>
			<div class="clear"></div>
		</div>
	</div>
</div>

