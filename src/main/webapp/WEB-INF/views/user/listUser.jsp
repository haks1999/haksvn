<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
</script>
<div class="content-page">
	<h1>Users</h1>
	<div class="col w10 last">
		<div class="content">
			<table id="tbl_users">
				<tr>
					<th width="100px">ID</th>
					<th>Name</th>
					<th>Email</th>
					<th width="50px">Active</th>
				</tr>
				<c:forEach items="${userList}" var="user">
					<tr>
						<td>
							<font class="path"><a href="<c:url value="/configuration/users/list/${user.userSeq}"/>"><c:out value="${user.userId}" /></a></font>
						</td>
						<td><c:out value="${user.userName}" /></td>
						<td><c:out value="${user.email}" /></td>
						<td>
							<haksvn:select name="active" codeGroup="common.boolean.yn.code" selectedValue="${user.active}" disabled="true" cssClass="readonly_list"></haksvn:select>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
	<div class="clear"></div>
</div>