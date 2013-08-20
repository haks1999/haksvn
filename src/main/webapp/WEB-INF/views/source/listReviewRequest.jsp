<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		$("#sel_repository").val('<c:out value="${repositoryKey}" />');
		$("#frm_reviewRequest select[name='rUser'] option[value='<c:out value="${reviewerId}" />']").attr('selected', 'selected');
		$("#frm_reviewRequest select[name='qUser'] option[value='<c:out value="${requestorId}" />']").attr('selected', 'selected');
		retrieveReviewRequestList();
		$("#sel_repository").change(changeRepository);
	});
	
	function changeRepository(){
		location.href = "<c:url value="/source/reviewRequest/list"/>" + "/" + $("#sel_repository > option:selected").val();
	};
	
	function retrieveReviewRequestList(){
		$("#tbl_reviewRequestList tfoot span:not(.loading)").removeClass('display-none').addClass('display-none');
		$("#tbl_reviewRequestList tfoot span.loader").removeClass('display-none');
		_paging.rUser = $("select[name='rUser'] option:selected").val();
		_paging.qUser = $("select[name='qUser'] option:selected").val();
		$.getJSON( "<c:url value="/source/reviewRequest/list/${repositoryKey}"/>",
				_paging,
				function(data) {
					var reviewRequestList = data.model;
					var repositoryKey = '<c:out value="${repositoryKey}" />';
					var hrefRoot = '<c:url value="/source/changes"/>';
					var path = '<c:out value="${path}" />';
					_paging.start = data.start + reviewRequestList.length;
					for( var inx = 0 ; inx < reviewRequestList.length ; inx++ ){
						var row = $("#tbl_reviewRequestList > tbody > .sample").clone();
						$(row).find(".revision a").attr('href',(hrefRoot + "/" + repositoryKey + (path.length<1?"":"/") + path + "?rev=" + reviewRequestList[inx].revision).replace("//", "/"));
						$(row).find(".revision font a").text('r'+reviewRequestList[inx].revision);
						var reviewersText = "";
						for( var jnx = 0 ; jnx < reviewRequestList[inx].reviewers.length ; jnx++ ){
							var reviewer = reviewRequestList[inx].reviewers[jnx];
							reviewersText += (reviewer.userName + "("+ reviewer.userId + ")");
							if( jnx < reviewRequestList[inx].reviewers.length -1 ) reviewersText += ", ";
						}
						$(row).children(".reviewers").text(reviewersText);
						$(row).children(".date").text(haksvn.date.convertToEasyFormat(new Date(reviewRequestList[inx].requestDate)));
						$(row).children(".requestor").text(reviewRequestList[inx].requestor.userId);
						$(row).removeClass("sample");
						$('#tbl_reviewRequestList > tbody').append(row);
					}
					
					if( reviewRequestList.length < 1 ){
						$("#tbl_reviewRequestList tfoot span:not(.nodata)").removeClass('display-none').addClass('display-none');
						$("#tbl_reviewRequestList tfoot span.nodata").removeClass('display-none');
					}else if( reviewRequestList.length < _paging.limit ){
						$("#tbl_reviewRequestList tfoot").removeClass('display-none').addClass('display-none');
					}else{
						$("#tbl_reviewRequestList tfoot span:not(.showmore)").removeClass('display-none').addClass('display-none');
						$("#tbl_reviewRequestList tfoot span.showmore").removeClass('display-none');
					};
		});
	};
	
	var _paging = {start:0,limit:15};
</script>
<div class="content-page">
	<div class="col w10 last">
		<div class="content">
			<div class="box">
				<div class="head"><div></div></div>
				<div class="desc search">
					<form id="frm_reviewRequest" method="get">
						<p>
							<label for="sel_repository" class="w_120">Repository Name</label> 
							<select id="sel_repository">
								<c:forEach items="${repositoryList}" var="repository">
									<option value="<c:out value="${repository.repositoryKey}"/>">
										<c:out value="${repository.repositoryName}" />
									</option>
								</c:forEach>
							</select>
							<label for="rUser" class="w_100">Reviewer</label> 
							<select name="rUser" class="all">
								<c:forEach items="${userList}" var="user">
									<option value="<c:out value="${user.userId}"/>">
										<c:out value="${user.userName}" />
									</option>
								</c:forEach>
							</select>
							<label for="qUser" class="w_100">Request User</label> 
							<select name="qUser" class="all">
								<c:forEach items="${userList}" var="user">
									<option value="<c:out value="${user.userId}"/>">
										<c:out value="${user.userName}" />
									</option>
								</c:forEach>
							</select>
							<a class="button right form_submit yellow"><small class="icon looking_glass"></small><span>Search</span></a>
						</p>
						<%--
						<p>
							<label for="path" class="w_120">Source path</label>
							<input id="ipt_path" name="path" type="text" class="text w_60"/>
							<a class="button right form_submit yellow"><small class="icon looking_glass"></small><span>Search</span></a>
						</p>
						 --%>
					</form>
				</div>
				<div class="bottom"><div></div></div>
			</div>
			
			<table id="tbl_reviewRequestList">
				<thead>
					<tr>
						<th width="100px">Revision</th>
						<th>Reviewers</th>
						<th width="100px">Date</th>
						<th width="100px">Requestor</th>
					</tr>
				</thead>
				<tbody>
					<tr class="sample">
						<td class="revision">
							<font class="path font12 open-window"><a href=""></a></font>
						</td>
						<td class="reviewers"></td>
						<td class="date"></td>
						<td class="requestor"></td>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="4" style="text-align:center;">
							<span class="showmore display-none"><font class="path"><a onclick="retrieveReviewRequestList()">Show More</a></font></span>
							<span class="loader display-none"><img src="<c:url value="/images/ajax-loader.gif"/>" /></span>
							<span class="nodata">no data</span>
						</td>
					</tr>
				</tfoot>
			</table>
			
		</div>
	</div>
	
	<div class="clear"></div>
</div>
