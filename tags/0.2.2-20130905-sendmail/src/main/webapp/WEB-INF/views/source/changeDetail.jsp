<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<style type="text/css">
#slider_score .ui-slider-range { background: #ddf8cc; }
#slider_score.ui-widget-content{background: #ffd8d8;}
#slider_score .ui-slider-handle {
    text-decoration: none;
    font-weight: bold;
    width: 25px;
    text-align: center;
    line-height:15px;
    font-size:12px;
}
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
	
	function openSearchUserDialog(){
		$("#div_searchUser select").val('').trigger("liszt:updated");
		$("#div_searchUser .chzn-container li.search-choice").remove();
		$.getJSON( "<c:url value="/common/users/find/${repositoryKey}"/>",
				{searchString: ""}, 
				function(data){
					$("#div_searchUser .chosen-select option").remove();
					for( var inx = 0 ; inx < data.length ; inx++ ){
						$("#div_searchUser .chosen-select").append("<option value=\"" +  data[inx].userId + "\">" + data[inx].userName + "(" + data[inx].userId + ")" + "</option>");
					}
					$("#div_searchUser .chosen-select").chosen({width:'380px'});
		    	}
		);
		
		$("#div_searchUser").dialog({
			resizable: false,
			height: 300,
			width: 450,
		    modal: true,
		    buttons: {
		          "Request": function() {
		        	  requestReviewToUsers();
		              $( this ).dialog( "close" );
		          },
		          Cancel: function() {
		              $( this ).dialog( "close" );
		          }
		        }
	    });
	};
	
	function requestReviewToUsers(){
		var selectedUsers = $("#div_searchUser .chosen-select").chosen().val();
		if(selectedUsers.length < 1) return;
		var userIds = {userId: selectedUsers};
		$.post("<c:url value="/source/review/${repositoryKey}/${svnSource.log.revision}/request"/>",
				$.param(userIds,true),
	            function(data){
					$().Message({type:data.type,text:data.text});
	        },"json");
	};
	
	
</script>
<c:set var="repoBrowsePathLink" value="${pageContext.request.contextPath}/source/browse/${repositoryKey}"/>
<c:set var="repoChangesPathLink" value="${pageContext.request.contextPath}/source/changes/${repositoryKey}"/>
<c:set var="repoDiffPathLink" value="${pageContext.request.contextPath}/source/changes/diff"/>
<div class="content-page">
	<h1></h1>
	<div class="col w10 last">
		<div class="content">
			<div class="box" style="position:absolute; display:block;width:300px;float:left;margin-right:-370px;left:10px;">
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
							<span><font class="path"><a onclick="expandAllChanged()">expand all</a></font></span>
							<span><font class="path"><a onclick="collapseAllChanged()">collapse all</a></font></span>
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
								<b class="mr10">Review Score</b>
								<font style="float:right;" class="path"><a onclick="openSearchUserDialog()">Request Review</a></font>
								<c:choose>
									<c:when test="${reviewSummary.isReviewed}">
										<c:if test="${reviewSummary.totalScore > 0}">
											<img src="<c:url value="/images/plus_green_square.png"/>"/>
										</c:if>
										<c:if test="${reviewSummary.totalScore < 0}">
											<img src="<c:url value="/images/minus_red_square.png"/>"/>
										</c:if>
										<span id="slider_score" class="display-inlineblock w_80 ml5" style="margin-bottom:-2px;"></span>
										<script type="text/javascript">
											$(function() {
												var totalScore = Number("<c:out value="${reviewSummary.totalScore}"/>");
											    $( "#slider_score" ).slider({
											    	range: "min",
											      	disabled:true,
											      	max: 1,
											      	min:-1,
											     	value: totalScore,
											     	create: function(){
											     		var slider = $( this ).data().slider;
											     	    slider.element.find( ".ui-slider-handle" ).text( (totalScore>0?"+":"") + totalScore);
											     	}
											    });
											});
										</script>
									</c:when>
									<c:otherwise>
										<span class="italic">No one did not reviewed yet.</span>
									</c:otherwise>
								</c:choose>
							</p>
							<c:if test="${reviewSummary.isReviewed}">
								<ul class="ml20">
									<li>
										<span class="display-inlineblock w_80 font12">Positive: </span>
										<span class="display-inlineblock italic font12">
											<c:forEach items="${reviewSummary.reviewScorePositiveList}" var="reviewScorePositive" varStatus="loop">
												<c:out value="${reviewScorePositive.reviewId.reviewer.userId}(${reviewScorePositive.reviewId.reviewer.userName})"/><c:if test="${!loop.last}">, </c:if>
											</c:forEach>
											<c:if test="${fn:length(reviewSummary.reviewScorePositiveList) == 0}">N/A</c:if>
										</span>
									</li>
									<li>
										<span class="display-inlineblock w_80 font12">Neutral: </span>
										<span class="display-inlineblock italic font12">
											<c:forEach items="${reviewSummary.reviewScoreNeutralList}" var="reviewScoreNeutral" varStatus="loop">
												<c:out value="${reviewScoreNeutral.reviewId.reviewer.userId}(${reviewScoreNeutral.reviewId.reviewer.userName})"/><c:if test="${!loop.last}">, </c:if>
											</c:forEach>
											<c:if test="${fn:length(reviewSummary.reviewScoreNeutralList) == 0}">N/A</c:if>
										</span>
									</li>
									<li>
										<span class="display-inlineblock w_80 font12">Negative: </span>
										<span class="display-inlineblock italic font12">
											<c:forEach items="${reviewSummary.reviewScoreNegativeList}" var="reviewScoreNegative" varStatus="loop">
												<c:out value="${reviewScoreNegative.reviewId.reviewer.userId}(${reviewScoreNegative.reviewId.reviewer.userName})"/><c:if test="${!loop.last}">, </c:if>
											</c:forEach>
											<c:if test="${fn:length(reviewSummary.reviewScoreNegativeList) == 0}">N/A</c:if>
										</span>
									</li>
								</ul>
							</c:if>
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
								<c:if test="${reviewComment.reviewCommentAuth.isDeletable}">
									<font style="float:right;" class="path"><a onclick="deleteReviewComment('<c:out value="${reviewComment.reviewCommentSeq}"/>')">delete</a></font>
									<script type="text/javascript">
										function deleteReviewComment(reviewCommentSeq){
											$.ajax( {
												type:'DELETE',
												url:"<c:url value="/source/review/${repositoryKey}/${svnSource.log.revision}/comment/"/>" + reviewCommentSeq,
												success: function(data){
													location.reload();
												}
											});
										}
									</script>
								</c:if>
							</p>
							<pre class="ml20"><c:out value="${reviewComment.comment}"/></pre>
						</div>
					</c:forEach>
					
					<c:if test="${reviewAuth.isCreatable}">
						<hr/>
						<h1>Your Review</h1>
						<form:form commandName="review" id="frm_review" method="post" modelAttribute="review" >
							<p>
								<form:label path="comment" class="left">Comment</form:label>
								<form:textarea class="text" cols="100" rows="3" path="comment"/>
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
											location.reload();
							        },"json");	
								};
							</script>
						</form:form>
					</c:if>
					
				</div>
			</div>
		
			
			
			
		</div>
	</div>
	
	<div class="clear"></div>
</div>

<div id="div_searchUser" title="Search User" style="display:none;">
	<div class="module text">
		<p>Select users for request review this change</p>
		<select data-placeholder="Find Users" class="chosen-select" multiple>
        </select>
	</div>
</div>
