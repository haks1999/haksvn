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
	
	function toggleRevisionLog(pmOpener){
		if($(pmOpener).hasClass('opened')){
			$(pmOpener).removeClass('opened').addClass('closed');
			$(pmOpener).parent().next('p').addClass('display-none');
		}else{
			$(pmOpener).removeClass('closed').addClass('opened');
			$(pmOpener).parent().next('p').removeClass('display-none');
		}
	}
	
	function toggleDetailInfo(toggler){
		if($(toggler).text() === 'Hide'){
			$(toggler).parent().parent().siblings('.division').css('display','none');
			$(toggler).text('Show');
		}else{
			$(toggler).parent().parent().siblings('.division').css('display','');
			$(toggler).text('Hide');
		}
	}
	
</script>
<c:set var="repoBrowsePathLink" value="${pageContext.request.contextPath}/source/browse/${repositorySeq}"/>
<c:set var="repoChangesPathLink" value="${pageContext.request.contextPath}/source/changes/${repositorySeq}"/>
<div id="table" class="help">
	<h1></h1>
	<div class="col w10 last">
		<div class="content">
			<div class="box">
				<div class="head"><div></div></div>
				<div class="desc">
					<p>
						<font class="path">Path:
							<c:set var="browsePathLink" value="${repoBrowsePathLink}"/>
							/<a href="${pathLink}">[SVN root]</a>
							<c:forEach var="pathFrag" items="${fn:split(svnSource.path, '/')}" varStatus="loop">
								<c:set var="browsePathLink" value="${pathLink}/${pathFrag}"/>
								<c:if test="${!loop.last}">/<a href="${pathLink}"><c:out value="${pathFrag}" /></a></c:if>
								<c:if test="${loop.last}">/<c:out value="${pathFrag}" /></c:if>
							</c:forEach>
						</font>
					</p>
				</div>
				<div class="bottom"><div></div></div>
			</div>
			
			 <div style="font-size:10pt;">
			 	<div class="box align-tr">
					<div class="head"><div></div></div>
					<div class="desc">
						<p>
							<font class="path" style="float:right;text-decoration:underline;cursor:pointer;"><a onclick="toggleDetailInfo(this)">Hide</a></font>
						</p>
						<div class="division">
							<p class="title">
								<span>Change Log</span>
							</p>
							<p>
								<span>
									<font class="path"><a href="${repoBrowsePathLink}/${svnSource.path}?rev=${svnSource.revision}">r<c:out value="${svnSource.revision}"/></a></font>
									&nbsp;by&nbsp;<c:out value="${svnSource.log.author}"/>
									,&nbsp;<c:out value="${svnSource.log.date}"/>
								</span>
							</p>
							<p>
								<span><c:out value="${svnSource.log.message}"/></span>
							</p>
						</div>
						<div class="division">
							<p class="title">
								<span>Revisions</span>
							</p>
							<c:forEach var="newerLog" items="${svnSource.newerLogs}">
								<p>
									<a class="pmOpener closed" onclick="toggleRevisionLog(this)">
										<img class="pClosed" src="<c:url value="/images/plus_small_white.png"/>"/><img class="mOpened" src="<c:url value="/images/minus_small_white.png"/>"/>
									</a>
									<span>
										<font class="path">
										<a href="${repoBrowsePathLink}/${svnSource.path}?rev=${newerLog.revision}">r<c:out value="${newerLog.revision}"/></a></font>
										&nbsp;by&nbsp;<c:out value="${newerLog.author}"/>
										,&nbsp;<c:out value="${newerLog.date}"/>,Diff
									</span>
								</p>
								<p class="display-none">
									<span>
										<c:out value="${newerLog.message}"/><br/>
										<font class="path"><a href="${repoBrowsePathLink}/${svnSource.path}?rev=${newerLog.revision}">View File</a></font>
									</span>
								</p>
								
							</c:forEach>
							<p>
								<a class="pmOpener closed visible-hidden">
									<img class="pClosed" src="<c:url value="/images/plus_small_white.png"/>"/>
								</a>
								<span><font class="path"><a>r<c:out value="${svnSource.revision}"/>&nbsp;Current Revision</a></font></span>
							</p>
							<c:forEach var="olderLog" items="${svnSource.olderLogs}">
								<p>
									<a class="pmOpener closed" onclick="toggleRevisionLog(this)">
										<img class="pClosed" src="<c:url value="/images/plus_small_white.png"/>"/><img class="mOpened" src="<c:url value="/images/minus_small_white.png"/>"/>
									</a>
									<span>
										<font class="path"><a href="${repoBrowsePathLink}/${svnSource.path}?rev=${olderLog.revision}">r<c:out value="${olderLog.revision}"/></a></font>
										&nbsp;by&nbsp;<c:out value="${olderLog.author}"/>
										,&nbsp;<c:out value="${olderLog.date}"/>,Diff
									</span>
								</p>
								<p class="display-none">
									<span>
										<c:out value="${olderLog.message}"/><br/>
										<font class="path"><a href="${repoBrowsePathLink}/${svnSource.path}?rev=${olderLog.revision}">View File</a></font>
									</span>
								</p>
							</c:forEach>
							<p>
								<span>
									<font class="path"><a href="${repoChangesPathLink}/${svnSource.path}">All revisions of this file</a></font>
								</span>
							</p>
						</div>
						<div class="division">
							<p class="title">
								<span>File Info</span>
							</p>
							<p>
								<span>
									Size:&nbsp;<fmt:formatNumber value="${svnSource.size}" type="NUMBER" groupingUsed="true" />&nbsp;bytes
								</span>
							</p>
						</div>
					</div>
					<div class="bottom"><div></div></div>
				</div>
			 	<pre id="pre_fileContent"><c:out value="${svnSource.content}" /></pre>
			</div>
			
		</div>
	</div>
	
	<div class="clear"></div>
</div>