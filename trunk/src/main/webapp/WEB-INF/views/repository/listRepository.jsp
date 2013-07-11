<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
</script>
<div class="content-page">
	<h1>Repositories</h1>
	<div class="col w10 last">
		<div class="content">
			<table id="tbl_repositories">
				<thead>
					<tr>
						<th>Name</th>
						<th>Location</th>
						<th>Sync User</th>
						<th>Active</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${repositoryList}" var="repository">
						<tr>
							<td>
								<font class="path"><a href="<c:url value="/configuration/repositories/list/${repository.repositorySeq}"/>"><c:out value="${repository.repositoryName}" /></a></font>
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