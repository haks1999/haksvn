<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		//$("#sel_repository").val('<c:out value="${repositorySeq}" />');
		$("#sel_repository option[value='<c:out value="${repositorySeq}" />']").attr('selected', 'selected');
		$("#frm_transfer select[name='rUser'] option[value='<c:out value="${requestUserId}" />']").attr('selected', 'selected');
		$("#frm_transfer select[name='sCode'] option[value='<c:out value="${transferStateCodeId}" />']").attr('selected', 'selected');
		if( '<c:out value="${repositorySeq}" />'.length > 0 ) retrieveTransferList();
		
		$("#sel_repository").change(changeRepository);
	});
	
	function changeRepository(){
		$("#frm_transfer").attr('action','<c:url value="/transfer/request/list"/>' + '/' + $("#sel_repository option:selected").val());
		$("#frm_transfer").submit();
	};
	
	function retrieveTransferList(){
		$("#tbl_transferList tfoot span:not(.loading)").removeClass('display-none').addClass('display-none');
		$("#tbl_transferList tfoot span.loader").removeClass('display-none');
		_paging.rUser = $("#frm_transfer select[name='rUser'] option:selected").val();
		_paging.sCode = $("#frm_transfer select[name='sCode'] option:selected").val();
		$.post( "<c:url value="/transfer/request/list"/>" + "/" + '<c:out value="${repositorySeq}" />',
				_paging,
				function(data) {
					//if( data.type && data.text ){
						//$().Message({type:data.type,text:data.text});
						//return;
					//}
					var transferList = data.model;
					_paging.start = data.start + transferList.length;
					for( var inx = 0 ; inx < transferList.length ; inx++ ){
						var row = $("#tbl_transferList > tbody > .sample").clone();
						$(row).find(".transferSeq a").text('req-'+transferList[inx].transferSeq);
						$(row).children(".transferType").text(transferList[inx].transferTypeCode.codeName);
						$(row).children(".transferState").text(transferList[inx].transferStateCode.codeName);
						$(row).children(".requestor").text(transferList[inx].requestUser.userName);
						$(row).children(".description").text(transferList[inx].description);
						if(transferList[inx].requestDate > 0) $(row).children(".requestDate").text(haksvn.date.convertToEasyFormat(new Date(transferList[inx].requestDate)));
						if(transferList[inx].transferDate > 0) $(row).children(".transferDate").text(haksvn.date.convertToEasyFormat(new Date(transferList[inx].transferDate)));
						$(row).attr('transferSeq',transferList[inx].transferSeq).attr('repositorySeq',transferList[inx].repositorySeq);
						$(row).click(function(){
							location.href = '<c:url value="/transfer/request/list"/>' + '/' + $(this).attr('repositorySeq') + '/' +  $(this).attr('transferSeq');
						});
						$(row).removeClass("sample");
						$('#tbl_transferList > tbody').append(row);
					}
					if( transferList.length < 1 ){
						$("#tbl_transferList tfoot span:not(.nodata)").removeClass('display-none').addClass('display-none');
						$("#tbl_transferList tfoot span.nodata").removeClass('display-none');
					}else if( transferList.length < _paging.limit ){
						$("#tbl_transferList tfoot").removeClass('display-none').addClass('display-none');
					}else{
						$("#tbl_transferList tfoot span:not(.showmore)").removeClass('display-none').addClass('display-none');
						$("#tbl_transferList tfoot span.showmore").removeClass('display-none');
					}
					
		},'json');
	};
	
	var _paging = {start:0,limit:15};
</script>




<div id="table" class="help">
	<div class="col w10 last">
		<div class="content">
			<div class="box">
				<div class="head"><div></div></div>
				<div class="desc search">
					<form id="frm_transfer" method="get">
						<p>
							<label for="sel_repository" class="w_110">Repository Name</label> 
							<select id="sel_repository">
								<c:forEach items="${repositoryList}" var="repository">
									<option value="<c:out value="${repository.repositorySeq}"/>">
										<c:out value="${repository.repositoryName}" />
									</option>
								</c:forEach>
							</select>
							<label for="rUser" class="w_100">Request User</label> 
							<select name="rUser" class="all">
								<c:forEach items="${userList}" var="user">
									<option value="<c:out value="${user.userId}"/>">
										<c:out value="${user.userName}" />
									</option>
								</c:forEach>
							</select>
							<label for="sCode" class="w_50">State</label> 
							<haksvn:select name="sCode" codeGroup="transfer.state.code" selectedValue="${transferStateCodeId}" cssClass="all"></haksvn:select>
						</p>
						<p>
							<label for="path" class="w_110">Source path</label>
							<input name="path" type="text" class="text w_60"/>
							<a class="button right form_submit"><small class="icon looking_glass"></small><span>Search</span></a>
						</p>
					</form>
				</div>
				<div class="bottom"><div></div></div>
			</div>
			
			<table id="tbl_transferList">
				<thead>
					<tr>
						<th>seq</th>
						<th>type</th>
						<th>state</th>
						<th>requestor</th>
						<th>description</th>
						<th>request date</th>
						<th>transfer date</th>
					</tr>
				</thead>
				<tbody>
					<tr class="sample clickable">
						<td class="transferSeq w_80"><font class="path"><a></a></font></td>
						<td class="transferType w_80"></td>
						<td class="transferState w_70"></td>
						<td class="requestor w_90"></td>
						<td class="description"></td>
						<td class="requestDate w_80" style="text-align:center;"></td>
						<td class="transferDate w_80" style="text-align:center;"></td>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="7" style="text-align:center;">
							<span class="showmore display-none"><font class="path"><a onclick="retrieveTransferList()">Show More</a></font></span>
							<span class="loader display-none"><img src="/haksvn/images/ajax-loader.gif" /></span>
							<span class="nodata">no data</span>
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
	<c:set var="createTransferPathLink" value="${pageContext.request.contextPath}/transfer/request/list/${repositorySeq}/add"/>
	<a href="<c:out value="${createTransferPathLink}"/>" class="button green right"><small class="icon plus"></small><span>Create</span></a>
	<div class="clear"></div>
</div>