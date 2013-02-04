<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		retrieveRepositoryUserList();
		$("#sel_repository").change(retrieveRepositoryUserList);
   	});
	
	function retrieveRepositoryUserList(  ){
		var repositorySeq = $("#sel_repository > option:selected").val();
		$.getJSON( "<c:url value="/configuration/repositories/listUser"/>" + "/" + repositorySeq, function(data) {
			for( var inx = 0 ; inx < data.length ; inx++ ){
				var row = '<tr><td>' + data[inx].userId + '</td>' +
							'<td>' + data[inx].userName + '</td>' +
							'<td>' + data[inx].email + '</td>' +
							'<td>' + data[inx].authType + '</td></tr>';
				$('#tbl_users').append(row);
			}
		});
	}
</script>
<div id="table" class="help">
	<h1></h1>
	<div class="col w10 last">
		<div class="content">
			<div class="box">
				<div class="head"><div></div></div>
				<h2><!-- box header --></h2>
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
				<div class="bottom">
					<div></div>
				</div>
			</div>

			<table id="tbl_users">
				<tr>
					<th>ID</th>
					<th>Name</th>
					<th>Email</th>
					<th>User Authority</th>
				</tr>
				<c:forEach items="${userList}" var="user">
					<tr>
						<td>
							<a href="<c:url value="/configuration/users/list/${user.userSeq}"/>"><c:out value="${user.userId}" /></a>
						</td>
						<td><c:out value="${user.userName}" /></td>
						<td><c:out value="${user.email}" /></td>
						<td>
							<haksvn:select name="active" codeGroup="user_auth_type_code" selectedValue="${user.authType}" disabled="true" cssClass="readonly_list"></haksvn:select>
						</td>
					</tr>
				</c:forEach>
			</table>
			
			<p>
				<a class="button green mt ml form_submit"><small class="icon plus"></small><span>Add User</span></a>
				<a class="button red mt ml"><small class="icon cross"></small><span>Delete User</span></a>
			</p>
		</div>
	</div>
	<div class="clear"></div>
</div>