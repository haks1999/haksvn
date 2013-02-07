<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="leftmenu">
	<div class="menu_list">
		<div class="content">
			<div class="col w10">
				<c:forEach items="${leftMenuList}" var="leftMenu" varStatus="loop">
					<a href="<c:url value="${leftMenu.menuUrl}"/>"> <c:out value="${leftMenu.menuName}" /> </a>
					<hr />
					<!-- 
					<c:if test="${!loop.last}"><hr /></c:if>
					-->
				</c:forEach>
			</div>
			<div class="clear"></div>
		</div>
	</div>
</div>

