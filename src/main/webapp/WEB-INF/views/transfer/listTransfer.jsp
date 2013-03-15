<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		$("#sel_repository").val('<c:out value="${repositorySeq}" />');
		//retrieveRepositoryChangeList();
		//$("#sel_repository").change(changeRepository);
	});
</script>




<div id="table" class="help">
	<div class="col w10 last">
		<div class="content">
		
		
			<div class="box">
				<div class="head"><div></div></div>
				<div class="desc">
					<p>
						<label>Repository Name</label> 
						<select id="sel_repository">
							<c:forEach items="${repositoryList}" var="repository">
								<option value="<c:out value="${repository.repositorySeq}"/>">
									<c:out value="${repository.repositoryName}" />
								</option>
							</c:forEach>
						</select>
					</p>
				</div>
				<div class="bottom"><div></div></div>
			</div>
			
			
		
		
		
		
		
		
			<table>
				<tr>
					<th>seq</th>
					<th>type</th>
					<th>description</th>
					<th>request date</th>
					<th>result</th>
					<th>transfer date</th>
				</tr>
				<c:forEach items="${requestList }">
					<tr id="id_1">
						<td><c:out value="${transferSeq }" /></td>
						<td><c:out value="${requestType }" /></td>
						<td><c:out value="${description }" /></td>
						<td><c:out value="${requestDate }" /></td>
						<td><c:out value="${requestResult }" /></td>
						<td><c:out value="${transferDate }" /></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
	<a href="<c:url value="/transfer/request/createRequest"  />" class="button green right"><span>regist</span></a>
	<div class="clear"></div>
</div>