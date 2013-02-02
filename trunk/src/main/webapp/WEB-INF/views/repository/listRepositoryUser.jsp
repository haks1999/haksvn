<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	/*
	$(function() {
		$.getJSON( "<c:url value="/configuration/repositories/list"/>", function(data) {
			for( var inx = 0 ; inx < data.length ; inx++ ){
				var row = '<tr><td>' + data[inx].repositoryName + '</td>' +
							'<td>' + data[inx].repositoryLocation + '</td>' +
							'<td>' + data[inx].repositoryStatus + '</td></tr>';
				$('#tbl_repositories').append(row);
			}
		});
		
   	});
	*/
</script>
<div id="table" class="help">
	<h1></h1>
	<div class="col w10 last">
		<div class="content">
			<div class="box">
				<div class="head">
				</div>
				<h2><!-- box header --></h2>
				<div class="desc">
					<p>
						<label class="left">Repository Name</label> <select
							id="sel_repository">
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
							<c:out value="${user.active}" />
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
	<div class="clear"></div>
</div>