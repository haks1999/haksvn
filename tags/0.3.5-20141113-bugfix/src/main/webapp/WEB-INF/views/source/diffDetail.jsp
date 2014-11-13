<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		var isNewContent = $('#header-trg').length < 1;
		var isDeletedContent = $('#header-src').length < 1;
		if( isNewContent ){
			$('#header-src').parent().removeClass('display-none');
		}else if (isDeletedContent ){
			$('#header-trg').parent().removeClass('display-none');
		}else{
			$('pre.diff .header-src').append($('#header-src').clone());
			$('pre.diff .header-trg').append($('#header-trg').clone());
		}
		
		$("#ckb_showDiffOnly").click(function(){
			if( $(this).prop("checked")){
				$("pre.diff tbody tr").not(".isDiff").addClass("display-none");
				$("pre.diff tbody tr.isFirst").before("<tr class=\"isHide\"><td style=\"text-align:center;\" colspan=\"4\">···</td></tr>");
				$("pre.diff tbody tr.isLast").append("<tr class=\"isHide\"><td style=\"text-align:center;\" colspan=\"4\">···</td></tr>");
			}else{
				$("pre.diff tbody tr.isHide").remove();
				$("pre.diff tbody tr").removeClass("display-none");
			}
		});
	});
	
</script>
<c:set var="repoBrowsePathLink" value="${pageContext.request.contextPath}/source/browse/${repositoryKey}"/>
<c:set var="repoChangesPathLink" value="${pageContext.request.contextPath}/source/changes/${repositoryKey}"/>
<div class="content-page">
	<h1></h1>
	<div class="col w10 last">
		<div class="content">
			<div class="display-none">
			
				<c:if test="${not svnSourceDiff.isNewContent}">
					<div id="header-src" class="content">
						<div class="box">
							<div class="head"><div></div></div>
							<div class="desc">
								<c:set var="srcBrowsePath" value="/${repoBrowsePathLink}/${svnSourceSrc.path}"/>
								<c:set var="srcBrowsePath" value="${fn:replace(srcBrowsePath,'//','/')}"/>
								<c:set var="srcChangedPath" value="/${repoChangesPathLink}/${svnSourceSrc.path}"/>
								<c:set var="srcChangedPath" value="${fn:replace(srcChangedPath,'//','/')}"/>
								<c:set var="srcRevision" value="${svnSourceSrc.isCopied ? svnSourceSrc.copiedRevision:svnSourceSrc.revision}" />
								<p style="width:500px;text-align:left;">
									<font class="path open-window">
										<a href="<c:out value="${srcBrowsePath}?rev=${srcRevision}" />">
											<c:out value="${svnSourceSrc.path}"/>
										</a>
									</font>
								</p>
								<p>
									<font class="path open-window"><a href="<c:out value="${srcChangedPath}?rev=${srcRevision}" />"><c:out value="r${srcRevision}"/></a></font>
								</p>
							</div>
							<div class="bottom"><div></div></div>
						</div>
					</div>
				</c:if>
				<c:if test="${not svnSourceDiff.isDeletedContent}">
					<div id="header-trg" class="content">
						<div class="box">
							<div class="head"><div></div></div>
							<div class="desc">
								<c:set var="trgBrowsePath" value="/${repoBrowsePathLink}/${svnSourceTrg.path}"/>
								<c:set var="trgBrowsePath" value="${fn:replace(trgBrowsePath,'//','/')}"/>
								<c:set var="trgChangedPath" value="/${repoChangesPathLink}/${svnSourceTrg.path}"/>
								<c:set var="trgChangedPath" value="${fn:replace(trgChangedPath,'//','/')}"/>
								<c:set var="trgRevision" value="${svnSourceTrg.isCopied ? svnSourceTrg.copiedRevision:svnSourceTrg.revision}" />
								<p style="width:500px;text-align:left;">
									<font class="path open-window">
										<a href="<c:out value="${trgBrowsePath}?rev=${trgRevision}" />">
											<c:out value="${svnSourceTrg.path}"/>
										</a>
									</font>
								</p>
								<p>
									<font class="path open-window"><a href="<c:out value="${trgChangedPath}?rev=${trgRevision}" />"><c:out value="r${trgRevision}"/></a></font>
								</p>
							</div>
							<div class="bottom"><div></div></div>
						</div>
					</div>
				</c:if>
			</div>
			
			<p style="margin-left:25px;">
				<span class="italic text"><input id="ckb_showDiffOnly" type="checkbox" />show differences only</span>
			</p>
			<pre class="diff">${svnSourceDiff.diffToHtml}</pre>
		</div>
	</div>
	
	<div class="clear"></div>
</div>
