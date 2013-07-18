<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	
	$(function() {
		$(".up,.down").click(function(){
		    var row = $(this).parents("tr:first");
		    if ($(this).is(".up")) {
		        row.insertBefore(row.prev());
		    }else{
		        row.insertAfter(row.next());
		    }
		    var repositories = [];
		    var order = 1;
		    $("table tbody tr").each(function(){
		    	repositories.push({repositoryKey:$(this).attr("class"),repositoryOrder:order++});
		    });
		    
			$.post("<c:url value="/configuration/repositories/list/changeOrder"/>",
				{repositoryList:haksvn.json.stringfy(repositories)},
	            function(data){
					haksvn.block.off();
					$().Message({type:data.type,text:data.text});
	        },"json");
		});
	});

</script>
<div class="content-page">
	<h1>Repositories</h1>
	<div class="col w10 last">
		<div class="content">
			<table>
				<thead>
					<tr>
						<th width="80px">Order</th>
						<th width="80px">Key</th>
						<th>Name</th>
						<th>Location</th>
						<th width="80px">Sync User</th>
						<th width="50px">Active</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${repositoryList}" var="repository">
						<tr class="${repository.repositoryKey}">
							<td>
								<font class="path"><a class="up">Up</a></font>
           						<font class="path"><a class="down">Down</a></font>
							</td>
							<td><c:out value="${repository.repositoryKey}" /></td>
							<td>
								<font class="path"><a href="<c:url value="/configuration/repositories/list/${repository.repositoryKey}"/>"><c:out value="${repository.repositoryName}" /></a></font>
							</td>
							<td><c:out value="${repository.repositoryLocation}" /></td>
							<td>
								<haksvn:select name="syncUser" codeGroup="common.boolean.yn.code" selectedValue="${repository.syncUser}" disabled="true" cssClass="readonly_list"></haksvn:select>
							</td>
							<td>
								<haksvn:select name="active" codeGroup="common.boolean.yn.code" selectedValue="${repository.active}" disabled="true" cssClass="readonly_list"></haksvn:select>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<div class="clear"></div>
</div>