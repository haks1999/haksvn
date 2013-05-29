<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="header">
	<div class="col w5 bottomlast">
		<a href="<c:url value="/" />" class="logo">
			<img src="<c:url value="/images/haksvn-logo.png"/>" style="height:25px;margin-bottom:10px;" alt="Haksvn Home" />
		</a>
	</div>
	<div class="col w5 last right bottomlast">
		<p class="last">
			<%
				com.haks.haksvn.common.security.model.LoginUser loginUser = com.haks.haksvn.common.security.util.ContextHolder.getLoginUser();
				if( loginUser != null ){
			%>
				Logged in as <span class="strong" style="margin-right:20px;"><%=loginUser.getUserName()%></span><a href="<c:url value="/logout"/>">Logout</a>
			<%
				}
			%>
		</p>
	</div>
	<div class="clear"></div>
</div>