<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
	<h1>Repositories</h1>
	<div class="col w10 last">
		<div class="content">
			<table id="tbl_repositories">
				<tr>
					<th>Name</th>
					<th>Location</th>
					<th>Status</th>
				</tr>
				<c:forEach items="${repositoryList}" var="repository">
					<tr>
						<td><c:out value="${repository.repositoryName}" /></td>
						<td><c:out value="${repository.repositoryLocation}" /></td>
						<td><c:out value="${repository.repositoryStatus}" /></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
	<div class="clear"></div>
</div>