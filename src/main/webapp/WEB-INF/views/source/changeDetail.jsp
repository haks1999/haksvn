<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<style type="text/css">

</style>
<script type="text/javascript">
	$(function() {
		$('.logDate').text(haksvn.date.convertToComplexFullFormat(new Date(Number('<c:out value="${svnSource.log.date}"/>'))));
	});
	
	function retrieveDiffWithPrevious(pmOpener, path, rev){
		$.getJSON( "<c:url value="/source/changes/diff"/>",
				{repositoryKey: '<c:out value="${repositoryKey}" />',
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
<c:set var="repoBrowsePathLink" value="${pageContext.request.contextPath}/source/browse/${repositoryKey}"/>
<c:set var="repoChangesPathLink" value="${pageContext.request.contextPath}/source/changes/${repositoryKey}"/>
<c:set var="repoDiffPathLink" value="${pageContext.request.contextPath}/source/changes/diff"/>
<div class="content-page">
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
							<span><b>Author: </b><c:out value="${svnSource.log.author}"/></span>
						</p>
						<p>
							<span><b>Date: </b><span class="logDate"></span></span>
						</p>
						<p class="mt10 mr10 mb10 ml10" style="margin:10px;">
							<span class="pre font-consolas"><c:out value="${svnSource.log.message}"/></span>
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
								<span>
									<span style="display:inline-block;width:53px;"><c:out value="${changed.typeName}" /></span>
									<font class="path font12 open-window">
										<c:set var="changedFullPath" value="${repoBrowsePathLink}/${changed.path}?rev=${svnSource.log.revision}" />
										<a href="${fn:replace(changedFullPath,'//','/')}"><c:out value="${changed.path}"/></a>
									</font>
									<font class="path font12 open-window">
										<a href="${repoDiffPathLink}?repositoryKey=${repositoryKey}&trgRev=${svnSource.revision}&srcRev=-1&path=${changed.path}">Diff</a>
									</font>
								</span>
							</p>
							<pre class="diff display-none">
							</pre>
						</c:forEach>
						
					</div>
					
					<div id="div_syncTaggingInfo" class="info mt50">
						<div class="tl"></div>
						<div class="tr"></div>
						<div class="desc variable-help">
							<p>
								<b>Review Scores: </b><c:out value="${reviewSummary.totalScore}"/>
							</p>
							<ul class="ml20">
								<li>
									<span class="display-inlineblock w_80 font12">Positive: </span>
									<span class="display-inlineblock italic font12">
										<c:forEach items="${reviewSummary.reviewScorePositiveList}" var="reviewScorePositive" varStatus="loop">
											<c:out value="${reviewScorePositive.reviewId.reviewer.userId}(${reviewScorePositive.reviewId.reviewer.userName})"/><c:if test="${!loop.last}">, </c:if>
										</c:forEach>
									</span>
								</li>
								<li>
									<span class="display-inlineblock w_80 font12">Neutral: </span>
									<span class="display-inlineblock italic font12">
										<c:forEach items="${reviewSummary.reviewScoreNeutralList}" var="reviewScoreNeutral" varStatus="loop">
											<c:out value="${reviewScoreNeutral.reviewId.reviewer.userId}(${reviewScoreNeutral.reviewId.reviewer.userName})"/><c:if test="${!loop.last}">, </c:if>
										</c:forEach>
									</span>
								</li>
								<li>
									<span class="display-inlineblock w_80 font12">Negative: </span>
									<span class="display-inlineblock italic font12">
										<c:forEach items="${reviewSummary.reviewScoreNegativeList}" var="reviewScoreNegative" varStatus="loop">
											<c:out value="${reviewScoreNegative.reviewId.reviewer.userId}(${reviewScoreNegative.reviewId.reviewer.userName})"/><c:if test="${!loop.last}">, </c:if>
										</c:forEach>
									</span>
								</li>
							</ul>
						</div>
						<div class="bl"></div>
						<div class="br"></div>
					</div>
					
					<jsp:useBean id="commentDate" class="java.util.Date" />
					<c:forEach items="${reviewSummary.reviewCommentList}" var="reviewComment" varStatus="loop">
						<jsp:setProperty name="commentDate" property="time" value="${reviewComment.commentDate}" />
						<hr/>
						<div class="mb20">
							<p>
								<font class="default">Comment by <b class="reviewer"><c:out value="${reviewComment.reviewer.userId}(${reviewComment.reviewer.userName})"/></b>, <b class="commentDate"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${commentDate}" /></b></font>
								<font class="path"><a>delete</a></font>
							</p>
							<pre class="ml20"><c:out value="${reviewComment.comment}"/></pre>
						</div>
					</c:forEach>
					
					<hr/>
					<h1>Your Review</h1>
					<form:form commandName="review" id="frm_review" method="post" modelAttribute="review" >
						<p>
							<form:label path="comment" class="left">Comment</form:label>
							<form:textarea class="text" cols="50" rows="5" path="comment"/>
							<form:errors path="comment" />
							<span class="form-status"></span>
						</p>
						<p>
							<form:label path="score" class="left">Score</form:label>
							<c:forEach items="${requestScope['review.score.code']}" var="reviewScoreCode">
								<form:radiobutton path="score" value="${reviewScoreCode.codeValue}"/><c:out value="${reviewScoreCode.codeName}"/>
							</c:forEach>  
							<input type="text" class="text visible-hidden"/>
						</p>
						<input type="button" value="submit" onclick="saveReview()"/>
						<script type="text/javascript">
							function saveReview(){
								var queryString = $('#frm_review').serialize();
								$.post("<c:url value="/source/review/${repositoryKey}/${svnSource.log.revision}"/>",
									queryString,
						            function(data){
										haksvn.block.off();
										//$().Message({type:data.type,text:data.text});
						        },"json");	
							};
						</script>
					</form:form>
					
					
				</div>
			</div>
		
			
			
			
		</div>
	</div>
	
	<div class="clear"></div>
</div>
