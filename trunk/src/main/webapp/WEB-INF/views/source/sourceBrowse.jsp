<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		$("#sel_repository").val('<c:out value="${repositorySeq}" />');
		listRepositorySource();
		$("#sel_repository").change(changeRepository);
	});
	
	function changeRepository(){
		frm_repository.action = "<c:url value="/source/browse"/>" + "/" + $("#sel_repository > option:selected").val();
		frm_repository.submit();
	}
	
	function listRepositorySource(){
		$("#div_sourceTree").dynatree({
			onClick: function(node, event) {
				if(node.data.isFolder) node.expand();
				retrieveSourceList(node.data.fileChildren);
				return true;
		      },
            clickFolderMode: 1,
            selectMode: 1,
	        children:[{title:'[SVN]',path:'<c:out value="${path}" />',isLazy:true,isFolder:true, expand:true}],
			onLazyRead: function(node){
				$.getJSON(
					"<c:url value="/source/browse/list"/>",
					{repositorySeq: '<c:out value="${repositorySeq}" />', path:node.data.path},
		            function(result){
		            	if( !node.data.fileChildren ) node.data.fileChildren = [];
	                    res = [];
	                    for(var inx=0, len=result.length; inx<len; inx++){
	                        var source = result[inx];
	                        if(source.isFolder){
	                        	res.push({title: source.title, path: source.path, isFolder:source.isFolder, isLazy: source.isLazy
     								,fileChildren:[]});
	                        }else{
	                        	node.data.fileChildren.push(source);
	                        }
	                    }
	                    if(res.length < 1 ){
	                    	var expandObj = $('#div_sourceTree li[dtnode="'+node.toString()+'"] span.dynatree-expanded');
	                    	expandObj.removeClass('dynatree-expanded').removeClass('dynatree-has-children');
	                    	$(expandObj).find('span.dynatree-expander').removeClass('dynatree-expanded').addClass('dynatree-connector');
	                    	node.data.isLazy = false;
	                    }
	                    node.setLazyNodeStatus(DTNodeStatus_Ok);
	                    node.addChild(res);
	 		        	retrieveSourceList(node.data.fileChildren);
		            }
		        );
	 		}
        });
		$("#div_sourceTree").dynatree("getTree").reload();
		retrieveSourceList([]);
		$("#div_sourceTree").dynatree("getRoot").visit(function(node){
			node.reloadChildren();
		});
	};
	
	
	function retrieveSourceList(sourceNodeList){
		$("#tbl_sourceList tbody tr").not(".nodata").not(".sample").remove();
		if( !sourceNodeList || sourceNodeList == null ) return;
		$("#tbl_sourceList tbody tr[class~=nodata]").css('display',sourceNodeList.length < 1?'table-row':'none');
		var repositorySeq = '<c:out value="${repositorySeq}" />';
		var path = '<c:out value="${path}" />';
		var browseHrefRoot = '<c:url value="/source/browse"/>';
		var changesHrefRoot = '<c:url value="/source/changes"/>';
		for( var inx = 0 ; inx < sourceNodeList.length ; inx++ ){
			var row = $("#tbl_sourceList > tbody > .sample").clone();
			$(row).find(".name a").attr('href',(browseHrefRoot + "/" + repositorySeq + "/" + sourceNodeList[inx].path + "?rev=" + sourceNodeList[inx].revision).replace("//", "/"));
			$(row).find(".name font a").text(sourceNodeList[inx].name);
			$(row).children(".size").text(sourceNodeList[inx].formattedSize);
			$(row).find(".revision a").attr('href',(changesHrefRoot + "/" + repositorySeq + "/" + path + "?rev=" + sourceNodeList[inx].revision).replace("//", "/").replace("/?","?"));
			$(row).find(".revision font a").text('r'+sourceNodeList[inx].revision);
			$(row).children(".date").text(haksvn.date.convertToEasyFormat(new Date(sourceNodeList[inx].date)));
			$(row).children(".author").text(sourceNodeList[inx].author);
			$(row).removeClass("sample");
			$(row).css('display','');
			$('#tbl_sourceList > tbody').append(row);
		}
		
	};
	//SyntaxHighlighter.all();
</script>
<c:set var="repoBrowsePathLink" value="${pageContext.request.contextPath}/source/browse/${repositorySeq}"/>
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
					<font class="path">Path:
					<c:set var="pathLink" value="${repoBrowsePathLink}"/>
					/<a href="${pathLink}">[SVN root]</a>
					<c:forEach var="pathFrag" items="${fn:split(path, '/')}">
						<c:set var="pathLink" value="${pathLink}/${pathFrag}"/>
						/<a href="${pathLink}"><c:out value="${pathFrag}" /></a>
					</c:forEach>
				</font>
				</p>
			</div>
			
			<div class="box header" style="position:absolute;display:block;width:300px;float:left;margin-right:-370px;left:10px;">
				<div class="head"><div></div></div>
				<h2>
					Repository Tree
				</h2>
				<div class="desc">
					<div id="div_sourceTree" style="height:300px;" ></div>
				</div>
				<div class="bottom"><div></div></div>
			</div>
			
			<div style="float:left; width:100%;z-index:-1;">
				<div style="margin-left:320px;min-height:350px;">
					<table id="tbl_sourceList">
						<thead>
							<tr>
								<th width="300px">Filename</th>
								<th width="100px">Size</th>
								<th width="50px">Revision</th>
								<th width="150px">Date</th>
								<th width="100px">Author</th>
							</tr>
						</thead>
						<tbody>
							<tr class="nodata"><td colspan="5">No files in the selected directory.</td></tr>
							<tr class="sample">
								<td class="name">
									<font class="path font12 open-window"><a href=""></a></font>
								</td>
								<td class="size"></td>
								<td class="revision">
									<font class="path font12 open-window"><a href=""></a></font>
								</td>
								<td class="date"></td>
								<td class="author"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	

	<div class="clear"></div>
</div>
