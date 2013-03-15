<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		$("#sel_repository").val('<c:out value="${repositorySeq}" />');
		retrieveRepositoryChangeList();
		$("#sel_repository").change(changeRepository);
	});
	
	function changeRepository(){
		frm_repository.action = "<c:url value="/source/changes"/>" + "/" + $("#sel_repository > option:selected").val();
		frm_repository.submit();
	}
	
	function retrieveRepositoryChangeList(){
		$("#tbl_changeList tbody tr:not(.sample)").remove();
		initPagingInfo();
		_paging.path = '<c:out value="${path}" />';
		_paging.repositorySeq = $("#sel_repository > option:selected").val();
		$.getJSON( "<c:url value="/source/changes/list"/>",
				_paging,
				function(data) {
					var model = data.model;
					//_paging.total = data.total;
					_paging.start = data.start;
					_paging.end = data.end;
					_paging.hasNext = data.hasNext;
					_paging.hasPrev = data.hasPrev;
					changePagingInfo();
					var repositorySeq = '<c:out value="${repositorySeq}" />';
					var hrefRoot = '<c:url value="/source/changes"/>';
					var path = '<c:out value="${path}" />';
					for( var inx = 0 ; inx < model.length ; inx++ ){
						var row = $("#tbl_changeList > tbody > .sample").clone();
						$(row).find(".revision a").text('r'+model[inx].revision).attr('href',(hrefRoot + "/" + repositorySeq + (path.length<1?"":"/") + path + "?rev=" + model[inx].revision).replace("//", "/"));
						$(row).children(".message").text(model[inx].message);
						$(row).children(".date").text(model[inx].date);
						$(row).children(".author").text(model[inx].author);
						$(row).removeClass("sample");
						$('#tbl_changeList > tbody').append(row);
					}
		});
	};
	
	var _paging = {start:-1,end:-1,limit:50,direction:0};
	function initPagingInfo(){
		$('span.paging .start').text('');
		$('span.paging .end').text('');
		$('span.paging .older').css('display','none');
		$('span.paging .newer').css('display','none');
	}
	function changePagingInfo(){
		$('span.paging .start').text(_paging.start);
		$('span.paging .end').text(_paging.end);
		//$('span.paging .total').text(_paging.total);
		$('span.paging .older').css('display',_paging.hasNext?'inline':'none');
		$('span.paging .newer').css('display',_paging.hasPrev?'inline':'none');
	}
	function changePagingOlder(){
		//_paging.start = _paging.start + _paging.limit;
		_paging.start = _paging.end+1;
		_paging.direction = 0;
		retrieveRepositoryChangeList();
	}
	function changePagingNewer(){
		//_paging.start = _paging.start - _paging.limit;
		_paging.start = _paging.start-1;
		_paging.direction = -1;
		retrieveRepositoryChangeList();
	}
</script>
<form id="frm_repository" action=""></form>
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
			<div>
				<p>
					<span class="paging">
						<span>r<a class="start"></a> - r<a class="end"></a></span>
						<span class="underline italic link newer" onclick="changePagingNewer()">Newer</span>
						<span class="underline italic link older" onclick="changePagingOlder()">Older</span>
					</span>
					<font class="path">Path:
						<c:set var="pathLink" value="${pageContext.request.contextPath}/source/changes/${repositorySeq}"/>
						<c:forEach var="pathFrag" items="${fn:split(path, '/')}" varStatus="loop">
							<c:set var="pathLink" value="${pathLink}/${pathFrag}"/>
							<c:choose>
								<c:when test="${!loop.last}">/<a href="${pathLink}"><c:out value="${pathFrag}" /></a></c:when>
								<c:when test="${(loop.last) && (svnSource.isFolder)}">/<a href="${pathLink}"><c:out value="${pathFrag}" /></a></c:when>
								<c:otherwise>/<c:out value="${pathFrag}" /></c:otherwise>
							</c:choose>
						</c:forEach>
					</font>
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
						<td class="revision"><font class="path font12"><a href=""></a></font></td>
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
