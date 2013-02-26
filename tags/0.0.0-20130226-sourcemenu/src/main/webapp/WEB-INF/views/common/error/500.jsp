<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<div class="errorpage">
	<div class="leftcolumn">
		<h1>
			500<span>Internal Server Error</span>
		</h1>
	</div>
	<div class="rightcolumn">
		<h2>Oops! Server error...</h2>
		<p>
		Sorry, it appears there has been an internal server error with the page you've requested.
		</p>
		<br/><br/>
		<span style="font-style:italic;font-size:9pt;color:red;"><c:out value="${pageContext.exception}" /></span>
		<span style="font-style:italic;font-size:8pt;">
			<c:forEach var="trace" items="${pageContext.exception.stackTrace}">
				${trace}<br/>
			</c:forEach>
		</span>
		
	</div>
</div>