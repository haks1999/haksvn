<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		$("#div_sourceTreePanel").resizable({
	        minWidth: '300',
	        maxWidth: '600',
	        minHeight:'320',
	        maxHeight:'600',
	        resize: function(event, ui) {
	        	$("#div_sourceListPanel").css("margin-left", ($(this).width() + 20) +"px"); 
	        	$("#div_sourceTree").height(($(this).height()-8) + "px");
	        }
	    });
		
		$("#sel_repository").val('<c:out value="${repositoryKey}" />');
		listRepositorySource();
		$("#sel_repository").change(changeRepository);
	});
	
	function changeRepository(){
		location.href = "<c:url value="/source/browse"/>" + "/" + $("#sel_repository > option:selected").val();
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
					{repositoryKey: '<c:out value="${repositoryKey}" />', path:node.data.path},
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
		var repositoryKey = '<c:out value="${repositoryKey}" />';
		var path = '<c:out value="${path}" />';
		var browseHrefRoot = '<c:url value="/source/browse"/>';
		var changesHrefRoot = '<c:url value="/source/changes"/>';
		for( var inx = 0 ; inx < sourceNodeList.length ; inx++ ){
			var row = $("#tbl_sourceList > tbody > .sample").clone();
			$(row).find(".name a").attr('href',(browseHrefRoot + "/" + repositoryKey + "/" + sourceNodeList[inx].path + "?rev=" + sourceNodeList[inx].revision).replace("//", "/"));
			$(row).find(".name font a").text(sourceNodeList[inx].name);
			$(row).children(".size").text(sourceNodeList[inx].formattedSize);
			$(row).find(".revision a").attr('href',(changesHrefRoot + "/" + repositoryKey + "/" + path + "?rev=" + sourceNodeList[inx].revision).replace("//", "/").replace("/?","?"));
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
<c:set var="repoBrowsePathLink" value="${pageContext.request.contextPath}/source/browse/${repositoryKey}"/>
<div class="content-page">
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
								<option value="<c:out value="${repository.repositoryKey}"/>">
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
			
			<div id="div_sourceTreePanel" class="ui-widget-content" style="float:left;border:#999a9b 1px solid;height:320px;width:300px;position:absolute;margin-right:-370px;left:10px;">
				<div id="div_sourceTree" style="height:312px;" ></div>
			</div>
			
			<div style="float:left; width:100%;z-index:-1;">
				<div id="div_sourceListPanel" style="margin-left:320px;min-height:320px;">
					<table id="tbl_sourceList">
						<thead>
							<tr>
								<th>Filename</th>
								<th width="100px">Size</th>
								<th width="50px">Revision</th>
								<th width="100px">Date</th>
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
