<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		$("#sel_repository").val('<c:out value="${repositorySeq}" />');
		$("#frm_transfer select[name='rUser']").each(function(){
			$(this).val('<c:out value="${requestUserId}" />');
		});
		$("#frm_transfer select[name='sCode']").each(function(){
			$(this).val('<c:out value="${transferStateCodeId}" />');
		});
		//retrieveRepositoryChangeList();
		//$("#sel_repository").change(changeRepository);
	});
	
	function retrieveTransferList(){
		$("#tbl_transferList tbody tr:not(.sample)").remove();
		//_paging.path = '<c:out value="${path}" />';
		_paging.repositorySeq = $("#sel_repository > option:selected").val();
		_paging.rUser = $("#frm_transfer select[name='rUser']")[0].val();
		_paging.sCode = $("#frm_transfer select[name='sCode']")[0].val();
		$.getJSON( "<c:url value="/transfer/request/list"/>",
				_paging,
				function(data) {
					var transferList = data.model;
					_paging.start = data.start + transferList.length;
					for( var inx = 0 ; inx < transferList.length ; inx++ ){
						var row = $("#tbl_transferList > tbody > .sample").clone();
						$(row).find(".transferSeq a").text('req-'+transferList[inx].transferSeq);
						$(row).children(".transferType").text(transferList[inx].transferType);
						$(row).children(".description").text(transferList[inx].description);
						$(row).children(".requestDate").text(transferList[inx].requestDate);
						$(row).children(".transferState").text(transferList[inx].transferState);
						$(row).children(".transferDate").text(transferList[inx].transferDate);
						$(row).removeClass("sample");
						$('#tbl_changeList > tbody').append(row);
					}
		});
	};
	
	var _paging = {start:0,limit:50};
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
						<th>description</th>
						<th>request date</th>
						<th>result</th>
						<th>transfer date</th>
					</tr>
				</thead>
				<tbody>
					<tr class="sample">
						<td class="transferSeq"><font class="path"><a></a></font></td>
						<td class="transferType"></td>
						<td class="description"></td>
						<td class="requestDate"></td>
						<td class="transferState"></td>
						<td class="transferDate"></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<c:set var="createTransferPathLink" value="${pageContext.request.contextPath}/transfer/request/add/${repositorySeq}"/>
	<a href="<c:out value="${createTransferPathLink}"/>" class="button green right"><small class="icon plus"></small><span>Create</span></a>
	<div class="clear"></div>
</div>