<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<%@ include file="/WEB-INF/views/common/include/syntaxhighlighter.jspf"%>
<script type="text/javascript">
	$(function() {
		/*//using ajax
		var brush = getShBrush(data.path);//new SyntaxHighlighter.brushes.JScript();
        brush.init({ toolbar: false });
        $('#pre_fileContent').html(brush.getHtml(data.content));
        */
		$('#pre_fileContent').addClass('brush:'+getShBrush('<c:out value="${svnSource.path}" />'));
		SyntaxHighlighter.defaults['auto-links'] = false;
		SyntaxHighlighter.defaults['toolbar'] = false;
		SyntaxHighlighter.all();
	});
	
</script>
<div id="table" class="help">
	<h1></h1>
	<div class="col w10 last">
		<div class="content">
			<div class="box">
				<div class="head"><div></div></div>
				<div class="desc">
					<p>
						<font class="path">Path:
							<c:set var="pathLink" value="${pageContext.request.contextPath}/source/browse/${repositorySeq}"/>
							/<a href="${pathLink}">[SVN root]</a>
							<c:forEach var="pathFrag" items="${fn:split(svnSource.path, '/')}" varStatus="loop">
								<c:set var="pathLink" value="${pathLink}/${pathFrag}"/>
								<c:if test="${!loop.last}">/<a href="${pathLink}"><c:out value="${pathFrag}" /></a></c:if>
								<c:if test="${loop.last}">/<c:out value="${pathFrag}" /></c:if>
							</c:forEach>
						</font>
					</p>
				</div>
				<div class="bottom"><div></div></div>
			</div>
			
			 <div style="font-size:10pt;">
			 	<pre id="pre_fileContent"><c:out value="${svnSource.content}" /></pre>
			</div>
			
		</div>
	</div>
	
	<div class="clear"></div>
</div>
