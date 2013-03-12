<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
	});
	
</script>
<c:set var="repoBrowsePathLink" value="${pageContext.request.contextPath}/source/browse/${repositorySeq}"/>
<c:set var="repoChangesPathLink" value="${pageContext.request.contextPath}/source/changes/${repositorySeq}"/>
<div id="table" class="help">
	<h1></h1>
	<div class="col w10 last">
		<div class="content">
		
		
			<div>
				<div class="col w5">
					<div class="content" style="padding-right:10px;">
						<div class="box">
							<div class="head"><div></div></div>
							<div class="desc">
								<c:set var="srcBrowsePath" value="/${repoBrowsePathLink}/${svnSourceTrg.path}"/>
								<c:set var="srcBrowsePath" value="${fn:replace(srcBrowsePath,'//','/')}"/>
								<c:set var="srcChangedPath" value="/${repoChangesPathLink}/${svnSourceTrg.path}"/>
								<c:set var="srcChangedPath" value="${fn:replace(srcChangedPath,'//','/')}"/>
								<p>
									<font class="path"><a href="<c:out value="${srcBrowsePath}?rev=${svnSourceSrc.revision}" />"><c:out value="${svnSourceSrc.path}"/></a></font>
								</p>
								<p>
									<font><a href="<c:out value="${srcChangedPath}?rev=${svnSourceTrg.revision}" />"><c:out value="r${svnSourceSrc.revision}"/></a></font>
								</p>
							</div>
							<div class="bottom"><div></div></div>
						</div>
					</div>
				</div>
				<div class="col w5 last">
					<div class="content" style="padding-left:10px;">
						<div class="box">
							<div class="head"><div></div></div>
							<div class="desc">
								<c:set var="trgBrowsePath" value="/${repoBrowsePathLink}/${svnSourceTrg.path}"/>
								<c:set var="trgBrowsePath" value="${fn:replace(trgBrowsePath,'//','/')}"/>
								<c:set var="trgChangedPath" value="/${repoChangesPathLink}/${svnSourceTrg.path}"/>
								<c:set var="trgChangedPath" value="${fn:replace(trgChangedPath,'//','/')}"/>
								<p>
									<font class="path"><a href="<c:out value="${trgBrowsePath}?rev=${svnSourceTrg.revision}" />"><c:out value="${svnSourceTrg.path}"/></a></font>
								</p>
								<p>
									<font><a href="<c:out value="${trgChangedPath}?rev=${svnSourceTrg.revision}" />"><c:out value="r${svnSourceTrg.revision}"/></a></font>
								</p>
							</div>
							<div class="bottom"><div></div></div>
						</div>
					</div>
				</div>
			</div>
			
			<pre class="diff">
				${svnSourceDiff.diffToHtml}
			</pre>
			
			
		</div>
	</div>
	
	<div class="clear"></div>
</div>
