<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
	});
	
	
	
	function retrieveDiffWithPrevious(pmOpener, path, rev){
		$.getJSON( "<c:url value="/source/changes/diff"/>",
				{repositorySeq: '<c:out value="${repositorySeq}" />',
				path: path,
				rev: rev},
				function(data) {
					alert(data.diff);
					$(pmOpener).parent().next().find('td').text(data.diff);
		});
	}
	
	function toggleChangedPath(pmOpener, path, rev){
		if($(pmOpener).hasClass('opened')){
			$(pmOpener).removeClass('opened').addClass('closed');
			//$(pmOpener).parent().next('p').addClass('display-none');
		}else{
			$(pmOpener).removeClass('closed').addClass('opened');
			retrieveDiffWithPrevious(pmOpener, path,rev);
			//$(pmOpener).parent().next('p').removeClass('display-none');
		}
	}
	
</script>
<c:set var="repoBrowsePathLink" value="${pageContext.request.contextPath}/source/browse/${repositorySeq}"/>
<c:set var="repoChangesPathLink" value="${pageContext.request.contextPath}/source/changes/${repositorySeq}"/>
<div id="table" class="help">
	<h1></h1>
	<div class="col w10 last">
		<div class="content">
		
		
		
			<div class="box" style="position:absolute;display:block;width:300px;float:left;margin-right:-370px;left:10px;">
				<div class="head"><div></div></div>
				<div class="desc">
					<p>
						<c:if test="${not empty svnSource.olderLogs}">
							<span class="menu-tr">
								<font class="path">
									<a href="${repoChangesPathLink}/${svnSource.path}?rev=${svnSource.olderLogs[0].revision}"><c:out value="r${svnSource.olderLogs[0].revision}"/></a>&nbsp;&gt;&gt;
								</font>
							</span>
						</c:if>
						<c:if test="${not empty svnSource.newerLogs}">
							<span class="menu-tr">
								<font class="path">
									&lt;&lt;&nbsp;<a href="${repoChangesPathLink}/${svnSource.path}?rev=${svnSource.newerLogs[fn:length(svnSource.newerLogs)-1].revision}"><c:out value="r${svnSource.newerLogs[fn:length(svnSource.newerLogs)-1].revision}"/></a>
								</font>
							</span>
						</c:if>
					</p>
					<div class="division">
						<p class="title">
							<span>Revision <c:out value="r${svnSource.log.revision}"/></span>
						</p>
						<p>
							<span>Author: <c:out value="${svnSource.log.author}"/></span>
						</p>
						<p>
							<span>Date: <c:out value="${svnSource.log.date}"/></span>
						</p>
						<hr>
						<p>
							<span><c:out value="${svnSource.log.message}"/></span>
						</p>
					</div>
				</div>
				<div class="bottom"><div></div></div>
			</div>
		
		
			<div style="float:left; width:100%;z-index:-1;">
				<div style="margin-left:320px;min-height:350px;">
					<div class="box">
						<div class="head"><div></div></div>
						<div class="desc">
							<p>
								<font class="path">Path:
									<c:set var="pathLink" value="${repoChangesPathLink}"/>
									/<a href="${repoChangesPathLink}?rev=${svnSource.revision}">[SVN root]</a>
									<c:forEach var="pathFrag" items="${fn:split(path, '/')}" varStatus="loop">
										<c:set var="pathLink" value="${pathLink}/${pathFrag}"/>
										<c:choose>
											<c:when test="${!loop.last}">/<a href="${pathLink}?rev=${svnSource.revision}"><c:out value="${pathFrag}" /></a></c:when>
											<c:otherwise>/<c:out value="${pathFrag}" /></c:otherwise>
										</c:choose>
									</c:forEach>
								</font>
							</p>
						</div>
						<div class="bottom"><div></div></div>
					</div>
					<div>
						<p>
							<span><font class="path">expand all</font></span>
							<span><font class="path">collapse all</font></span>
						</p>
						
						<c:forEach var="changed" items="${svnSource.log.changedList}">
							<p>
								<a class="pmOpener closed" onclick="toggleChangedPath(this,'${changed.path}','${svnSource.log.revision}')">
									<img class="pClosed" src="<c:url value="/images/plus_small_white.png"/>"/><img class="mOpened" src="<c:url value="/images/minus_small_white.png"/>"/>
								</a>
								<span>
									<font class="path font12">
										<c:set var="changedFullPath" value="${repoBrowsePathLink}/${changed.path}?rev=${svnSource.log.revision}" />
										<a href="${fn:replace(changedFullPath,'//','/')}"><c:out value="${changed.path}"/></a>
									</font>
								</span>
							</p>
							<table>
									<tr><td>111</td></tr>
								</table>
						</c:forEach>
						
					</div>
				</div>
			</div>
		
		
		
		
		
		
		
		
		
		
		
		
		
		
			
			
			
		</div>
	</div>
	
	<div class="clear"></div>
</div>
