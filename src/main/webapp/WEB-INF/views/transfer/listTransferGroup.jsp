<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		$("#sel_repository option[value='<c:out value="${repositoryKey}" />']").attr('selected', 'selected');
		$("#frm_transferGroup select[name='sCode'] option[value='<c:out value="${transferGroupStateCodeId}" />']").attr('selected', 'selected');
		$("#frm_transferGroup select[name='tCode'] option[value='<c:out value="${transferGroupTypeCodeId}" />']").attr('selected', 'selected');
		$("#ipt_title").val('<c:out value="${title}" />');
		if( '<c:out value="${repositoryKey}" />'.length > 0 ) retrieveTransferGroupList();
		$("#sel_repository").change(changeRepository);
	});
	
	function changeRepository(){
		$("#frm_transferGroup").attr('action','<c:url value="/transfer/requestGroup/list"/>' + '/' + $("#sel_repository option:selected").val());
		$("#frm_transferGroup").submit();
	};
	
	function retrieveTransferGroupList(){
		$("#tbl_transferGroupList tfoot span:not(.loading)").removeClass('display-none').addClass('display-none');
		$("#tbl_transferGroupList tfoot span.loader").removeClass('display-none');
		_paging.sCode = $("#frm_transferGroup select[name='sCode'] option:selected").val();
		_paging.tCode = $("#frm_transferGroup select[name='tCode'] option:selected").val();
		_paging.title = $("#ipt_title").val();
		$.post( "<c:url value="/transfer/requestGroup/list"/>" + "/" + '<c:out value="${repositoryKey}" />',
				_paging,
				function(data) {
					var transferGroupList = data.model;
					_paging.start = data.start + transferGroupList.length;
					for( var inx = 0 ; inx < transferGroupList.length ; inx++ ){
						var row = $("#tbl_transferGroupList > tbody > .sample").clone();
						$(row).find(".transferGroupSeq font a").text('group-'+transferGroupList[inx].transferGroupSeq);
						$(row).find(".transferGroupSeq a").attr("href",'<c:url value="/transfer/requestGroup/list/${repositoryKey}/"/>' +  transferGroupList[inx].transferGroupSeq);
						$(row).children(".transferGroupType").text(transferGroupList[inx].transferGroupTypeCode.codeName);
						$(row).children(".transferGroupState").text(transferGroupList[inx].transferGroupStateCode.codeName);
						$(row).children(".title").text(transferGroupList[inx].title);
						if(transferGroupList[inx].transferUser) $(row).children(".transferrer").text(transferGroupList[inx].transferUser.userName);
						if(transferGroupList[inx].transferDate > 0) $(row).children(".transferDate").text(haksvn.date.convertToEasyFormat(new Date(transferGroupList[inx].transferDate)));
						$(row).attr('transferGroupSeq',transferGroupList[inx].transferGroupSeq).attr('repositoryKey',transferGroupList[inx].repositoryKey);
						$(row).removeClass("sample");
						$('#tbl_transferGroupList > tbody').append(row);
					}
					if( transferGroupList.length < 1 ){
						$("#tbl_transferGroupList tfoot span:not(.nodata)").removeClass('display-none').addClass('display-none');
						$("#tbl_transferGroupList tfoot span.nodata").removeClass('display-none');
					}else if( transferGroupList.length < _paging.limit ){
						$("#tbl_transferGroupList tfoot").removeClass('display-none').addClass('display-none');
					}else{
						$("#tbl_transferGroupList tfoot span:not(.showmore)").removeClass('display-none').addClass('display-none');
						$("#tbl_transferGroupList tfoot span.showmore").removeClass('display-none');
					}
					
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
					<form id="frm_transferGroup" method="get">
						<p>
							<label for="sel_repository" class="w_120">Repository Name</label> 
							<select id="sel_repository">
								<c:forEach items="${repositoryList}" var="repository">
									<option value="<c:out value="${repository.repositoryKey}"/>">
										<c:out value="${repository.repositoryName}" />
									</option>
								</c:forEach>
							</select>
							<label for="sCode" class="w_50">State</label> 
							<haksvn:select name="sCode" codeGroup="transfergroup.state.code" selectedValue="${transferGroupStateCodeId}" cssClass="all"></haksvn:select>
							<label for="tCode" class="w_50">Type</label> 
							<haksvn:select name="tCode" codeGroup="transfergroup.type.code" selectedValue="${transferGroupTypeCodeId}" cssClass="all"></haksvn:select>
						</p>
						<p>
							<label for="title" class="w_120">Group Title</label>
							<input id="ipt_title" name="title" type="text" class="text w_500"/>
							<a class="button right form_submit yellow"><small class="icon looking_glass"></small><span>Search</span></a>
						</p>
					</form>
				</div>
				<div class="bottom"><div></div></div>
			</div>
			
			<table id="tbl_transferGroupList">
				<thead>
					<tr>
						<th>ID</th>
						<th>Type</th>
						<th>State</th>
						<th>Title</th>
						<th>Transferrer</th>
						<th>Transfer Date</th>
					</tr>
				</thead>
				<tbody>
					<tr class="sample">
						<td class="transferGroupSeq w_80"><font class="path open-window"><a></a></font></td>
						<td class="transferGroupType w_80"></td>
						<td class="transferGroupState w_70"></td>
						<td class="title"></td>
						<td class="transferrer w_90"></td>
						<td class="transferDate w_90" style="text-align:center;"></td>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="6" style="text-align:center;">
							<span class="showmore display-none"><font class="path"><a onclick="retrieveTransferGroupList()">Show More</a></font></span>
							<span class="loader display-none"><img src="<c:url value="/images/ajax-loader.gif"/>"/></span>
							<span class="nodata">no data</span>
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
	<haksvn:access admin="true" reviewer="true" commiter="false">
		<c:set var="createTransferGroupPathLink" value="${pageContext.request.contextPath}/transfer/requestGroup/list/${repositoryKey}/add"/>
		<a href="<c:out value="${createTransferGroupPathLink}"/>" class="button green right"><small class="icon plus"></small><span>Create</span></a>
	</haksvn:access>
	<div class="clear"></div>
</div>