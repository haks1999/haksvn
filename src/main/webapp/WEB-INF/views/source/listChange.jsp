<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		retrieveRepositoryChangeList();
		$("#sel_repository").change(retrieveRepositoryChangeList);
	});
	
	
	function retrieveRepositoryChangeList(){
		var repositorySeq = $("#sel_repository > option:selected").val();
		$("#tbl_changeList tbody tr:not(.sample)").remove();
		$.getJSON( "<c:url value="/source/changes"/>" + "/" + repositorySeq,
				_paging,
				function(data) {
					var model = data.model;
					_paging.total = data.total;
					changePagingInfo();
					for( var inx = 0 ; inx < model.length ; inx++ ){
						var row = $("#tbl_changeList > tbody > .sample").clone();
						$(row).children(".revision").text(model[inx].revision);
						$(row).children(".message").text(model[inx].message);
						$(row).children(".date").text(model[inx].date);
						$(row).children(".author").text(model[inx].author);
						$(row).removeClass("sample");
						$('#tbl_changeList > tbody').append(row);
					}
		});
	};
	
	var _paging = {start:0,limit:5,total:-1};
	function changePagingInfo(){
		var start = _paging.total-_paging.start-1;
		var end = start-_paging.limit+1;
		$('div.paging .start').text(start);
		$('div.paging .end').text(end);
		$('div.paging .total').text(_paging.total);
		$('div.paging .older').css('display',end > 0 ?'inline':'none');
	}
	function changePagingOlder(){
		_paging.start = _paging.start + _paging.limit;
		retrieveRepositoryChangeList();
	}
</script>
<div id="table" class="help">
	<h1></h1>
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
			<div class="paging">
				<p>
					<span>Total <a class="start"></a>-<a class="end"></a> of <a class="total"></a></span>
					<span class="underline italic link newer" onclick="changePagingNewer()">Newer</span>
					<span class="underline italic link older" onclick="changePagingOlder()">Older</span>
				</p>
			</div>
			<table id="tbl_changeList">
				<thead>
					<tr>
						<th width="100px">Revision</th>
						<th width="500px">Commit Log Message</th>
						<th width="150px">Date</th>
						<th width="100px">Author</th>
					</tr>
				</thead>
				<tbody>
					<tr class="sample">
						<td class="revision"></td>
						<td class="message"></td>
						<td class="date"></td>
						<td class="author"></td>
					</tr>
				</tbody>
			</table>
			
		</div>
	</div>
	
	<div class="clear"></div>
</div>
