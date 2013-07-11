<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<c:set var="repoBrowsePathLink" value="${pageContext.request.contextPath}/source/browse/${repositoryKey}"/>
<script type="text/javascript">
	$(function() {
		$("#sel_repository option[value='<c:out value="${repositoryKey}" />']").attr('selected', 'selected');
		$("#frm_tagging select[name='tUser'] option[value='<c:out value="${taggingUserId}" />']").attr('selected', 'selected');
		$("#frm_tagging select[name='tCode'] option[value='<c:out value="${taggingTypeCodeId}" />']").attr('selected', 'selected');
		if( '<c:out value="${repositoryKey}" />'.length > 0 ){
			retrieveTaggingList();
			retrieveLatestSyncTagging();
		}
		
		$("#sel_repository").change(changeRepository);
	});
	
	function changeRepository(){
		$("#frm_tagging").attr('action','<c:url value="/transfer/tagging/list"/>' + '/' + $("#sel_repository option:selected").val());
		$("#frm_tagging").submit();
	};
	
	function retrieveLatestSyncTagging(){
		$.post( "<c:url value="/transfer/tagging/list"/>" + "/" + '<c:out value="${repositoryKey}/latest" />',
				{},
				function(data) {
					if( !data ) {
						$("#div_syncTaggingInfo a").remove();
						return;
					}
					var taggingPath = data.taggingTypeCode.codeId == 'tagging.type.code.create' ?data.destPath:data.srcPath;
					$("#div_syncTaggingInfo font a").text(taggingPath);
					$("#div_syncTaggingInfo a").attr("href","<c:out value="${repoBrowsePathLink}" />" + taggingPath);
				},'json');
	};
	
	function retrieveTaggingList(){
		$("#tbl_taggingList tfoot span:not(.loading)").removeClass('display-none').addClass('display-none');
		$("#tbl_taggingList tfoot span.loader").removeClass('display-none');
		_paging.tUser = $("#frm_tagging select[name='tUser'] option:selected").val();
		_paging.tCode = $("#frm_tagging select[name='tCode'] option:selected").val();
		$.post( "<c:url value="/transfer/tagging/list"/>" + "/" + '<c:out value="${repositoryKey}" />',
				_paging,
				function(data) {
					
					var taggingList = data.model;
					if( _paging.start < 1 && taggingList.length > 0 ){
						$("#div_syncTaggingInfo font a").text(taggingList[0].taggingTypeCode.codeId == 'tagging.type.code.create' ?taggingList[0].destPath:taggingList[0].srcPath);
					}
					_paging.start = data.start + taggingList.length;
					for( var inx = 0 ; inx < taggingList.length ; inx++ ){
						var row = $("#tbl_taggingList > tbody > .sample").clone();
						$(row).find(".taggingSeq a").text('tagging-'+taggingList[inx].taggingSeq);
						$(row).children(".taggingType").text(taggingList[inx].taggingTypeCode.codeName);
						$(row).children(".tagger").text(taggingList[inx].taggingUser.userName);
						$(row).children(".tagName").text(taggingList[inx].tagName);
						$(row).children(".taggingDate").text(haksvn.date.convertToEasyFormat(new Date(taggingList[inx].taggingDate)));
						$(row).attr('taggingSeq',taggingList[inx].taggingSeq).attr('repositoryKey',taggingList[inx].repositoryKey);
						$(row).click(function(){
							location.href = '<c:url value="/transfer/tagging/list"/>' + '/' + $(this).attr('repositoryKey') + '/' +  $(this).attr('taggingSeq');
						});
						$(row).removeClass("sample");
						$('#tbl_taggingList > tbody').append(row);
					}
					if( taggingList.length < 1 ){
						$("#tbl_taggingList tfoot span:not(.nodata)").removeClass('display-none').addClass('display-none');
						$("#tbl_taggingList tfoot span.nodata").removeClass('display-none');
					}else if( taggingList.length < _paging.limit ){
						$("#tbl_taggingList tfoot").removeClass('display-none').addClass('display-none');
					}else{
						$("#tbl_taggingList tfoot span:not(.showmore)").removeClass('display-none').addClass('display-none');
						$("#tbl_taggingList tfoot span.showmore").removeClass('display-none');
					};
			},'json');
	};
	
	var _paging = {start:0,limit:15};
</script>

<div class="content-page">
	<div class="col w10 last">
		<div class="content">
			<div class="box">
				<div class="head"><div></div></div>
				<div class="desc search">
					<form id="frm_tagging" method="get">
						<p>
							<label for="sel_repository" class="w_120">Repository Name</label> 
							<select id="sel_repository">
								<c:forEach items="${repositoryList}" var="repository">
									<option value="<c:out value="${repository.repositoryKey}"/>">
										<c:out value="${repository.repositoryName}" />
									</option>
								</c:forEach>
							</select>
							<label for="tUser" class="w_100">Tagging User</label> 
							<select name="tUser" class="all">
								<c:forEach items="${userList}" var="user">
									<option value="<c:out value="${user.userId}"/>">
										<c:out value="${user.userName}" />
									</option>
								</c:forEach>
							</select>
							<label for="tCode" class="w_50">Type</label> 
							<haksvn:select name="tCode" codeGroup="tagging.type.code" selectedValue="${taggingTypeCodeId}" cssClass="all"></haksvn:select>
							<a class="button right form_submit yellow"><small class="icon looking_glass"></small><span>Search</span></a>
						</p>
					</form>
				</div>
				<div class="bottom"><div></div></div>
			</div>
			
			<div class="info-green" id="div_syncTaggingInfo">
				Latest Tag synchronized with production branch: <font class="path open-window"><a></a></font>
			</div>
			
			<table id="tbl_taggingList">
				<thead>
					<tr>
						<th>ID</th>
						<th>Type</th>
						<th>Tagger</th>
						<th>Tag Name</th>
						<th>Tagging Date</th>
					</tr>
				</thead>
				<tbody>
					<tr class="sample clickable">
						<td class="taggingSeq w_80"><font class="path"><a></a></font></td>
						<td class="taggingType w_80"></td>
						<td class="tagger w_90"></td>
						<td class="tagName"></td>
						<td class="taggingDate w_90" style="text-align:center;"></td>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="5" style="text-align:center;">
							<span class="showmore display-none"><font class="path"><a onclick="retrieveTaggingList()">Show More</a></font></span>
							<span class="loader display-none"><img src="<c:url value="/images/ajax-loader.gif"/>"/></span>
							<span class="nodata">no data</span>
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
	<c:if test="${taggingAuth.isCreatable}">
		<c:set var="createTaggingPathLink" value="${pageContext.request.contextPath}/transfer/tagging/list/${repositoryKey}/add"/>
		<a href="<c:out value="${createTaggingPathLink}"/>" class="button green right"><small class="icon plus"></small><span>Create</span></a>
	</c:if>
	<div class="clear"></div>
</div>