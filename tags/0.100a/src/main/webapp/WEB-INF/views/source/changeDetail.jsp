<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		$('.logDate').text('Date: ' + haksvn.date.convertToComplexFullFormat(new Date(Number('<c:out value="${svnSource.log.date}"/>'))));
	});
	
	function retrieveDiffWithPrevious(pmOpener, path, rev){
		$.getJSON( "<c:url value="/source/changes/diff"/>",
				{repositorySeq: '<c:out value="${repositorySeq}" />',
				path: path,
				rev: rev},
				function(data) {
					$(pmOpener).parent().next('pre').removeClass('loading').addClass('loaded');
					$(pmOpener).parent().next('pre').html(data.diffToHtml);
		});
	};
	
	function toggleChangedPath(pmOpener, path, rev){
		var pre = $(pmOpener).parent().next('pre');
		if($(pmOpener).hasClass('opened')){
			$(pmOpener).removeClass('opened').addClass('closed');
			$(pre).addClass('display-none');
		}else{
			$(pmOpener).removeClass('closed').addClass('opened');
			$(pre).removeClass('display-none');
			if( !$(pre).hasClass('loaded')){
				$(pre).addClass('loading');
				retrieveDiffWithPrevious(pmOpener, path,rev);
			}
		}
	};
	
	function expandAllChanged(){
		$('a.closed').trigger('click');
	};
	
	function collapseAllChanged(){
		$('a.opened').trigger('click');
	};
	
</script>
<c:set var="repoBrowsePathLink" value="${pageContext.request.contextPath}/source/browse/${repositorySeq}"/>
<c:set var="repoChangesPathLink" value="${pageContext.request.contextPath}/source/changes/${repositorySeq}"/>
<c:set var="repoDiffPathLink" value="${pageContext.request.contextPath}/source/changes/diff"/>
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
							<span class="logDate"></span>
						</p>
						<hr/>
						<p>
							<span class="pre"><c:out value="${svnSource.log.message}"/></span>
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
									<c:set var="listHeadChangesPathLink" value="${repoChangesPathLink}"/>
									<c:set var="listHeadBrowsePathLink" value="${repoBrowsePathLink}"/>
									/<a href="${repoChangesPathLink}?rev=${svnSource.revision}">[SVN root]</a>
									<c:forEach var="pathFrag" items="${fn:split(path, '/')}" varStatus="loop">
										<c:set var="listHeadChangesPathLink" value="${listHeadChangesPathLink}/${pathFrag}"/>
										<c:set var="listHeadBrowsePathLink" value="${listHeadBrowsePathLink}/${pathFrag}"/>
										<c:choose>
											<c:when test="${!loop.last}">/<a href="${listHeadChangesPathLink}?rev=${svnSource.revision}"><c:out value="${pathFrag}" /></a></c:when>
											<c:otherwise>
												/<c:out value="${pathFrag}" />
												<c:if test="${svnSource.isFolder}">
													&nbsp;<span class="italic"><a href="${listHeadBrowsePathLink}">(browsing)</a></span>
												</c:if>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</font>
							</p>
						</div>
						<div class="bottom"><div></div></div>
					</div>
					<div>
						<p>
							<span><font class="path"><a onclick="expandAllChanged()" style="text-decoration:underline;cursor:pointer;">expand all</a></font></span>
							<span><font class="path"><a onclick="collapseAllChanged()" style="text-decoration:underline;cursor:pointer;">collapse all</a></font></span>
						</p>
						
						<c:forEach var="changed" items="${svnSource.log.changedList}">
							<p>
								<a class="pmOpener closed" onclick="toggleChangedPath(this,'${changed.path}','${svnSource.log.revision}')">
									<img class="pClosed" src="<c:url value="/images/plus_small_white.png"/>"/><img class="mOpened" src="<c:url value="/images/minus_small_white.png"/>"/>
								</a>
								<span><c:out value="${changed.typeName}" />
									<font class="path font12 open-window">
										<c:set var="changedFullPath" value="${repoBrowsePathLink}/${changed.path}?rev=${svnSource.log.revision}" />
										<a href="${fn:replace(changedFullPath,'//','/')}"><c:out value="${changed.path}"/></a>
									</font>
									<font class="path font12 open-window">
										<a href="${repoDiffPathLink}?repositorySeq=${repositorySeq}&trgRev=${svnSource.revision}&srcRev=-1&path=${changed.path}">Diff</a>
									</font>
								</span>
							</p>
							<pre class="diff display-none">
							</pre>
						</c:forEach>
						
					</div>
				</div>
			</div>
		
			
			
			
		</div>
	</div>
	
	<div class="clear"></div>
</div>