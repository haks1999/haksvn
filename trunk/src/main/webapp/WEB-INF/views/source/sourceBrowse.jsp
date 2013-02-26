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
		$("#tbl_sourceList tbody tr:not(.sample)").remove();
		if( !sourceNodeList || sourceNodeList == null ) return;
		$("#tbl_sourceList tbody tr.sample").css('display',sourceNodeList.length < 1?'inline':'');
		var repositorySeq = '<c:out value="${repositorySeq}" />';
		var hrefRoot = '<c:url value="/source/browse"/>';
		for( var inx = 0 ; inx < sourceNodeList.length ; inx++ ){
			var row = $("#tbl_sourceList > tbody > .sample").clone();
			$(row).find(".name a").text(sourceNodeList[inx].name).attr('href',(hrefRoot + "/" + repositorySeq + "/" + sourceNodeList[inx].path + "?rev=" + sourceNodeList[inx].revision).replace("//", "/"));
			$(row).children(".size").text(sourceNodeList[inx].size);
			$(row).children(".revision").text(sourceNodeList[inx].revision);
			$(row).children(".date").text(sourceNodeList[inx].date);
			$(row).children(".author").text(sourceNodeList[inx].author);
			$(row).removeClass("sample");
			$(row).css('display','');
			$('#tbl_sourceList > tbody').append(row);
		}
		
	};
	//SyntaxHighlighter.all();
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
			
			<div class="box header" style="position:absolute;display:block;width:300px;float:left;margin-right:-370px;left:10px;">
				<div class="head"><div></div></div>
				<h2>
					<font class="path">Path:
						<c:set var="pathLink" value="${pageContext.request.contextPath}/source/browse/${repositorySeq}"/>
						<c:forEach var="pathFrag" items="${fn:split(path, '/')}">
							<c:set var="pathLink" value="${pathLink}/${pathFrag}"/>
							/<a href="${pathLink}"><c:out value="${pathFrag}" /></a>
						</c:forEach>
					</font>
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
							<tr class="sample" style="display:inline;">
								<td class="name"><a href="">No files in the selected directory.</a></td>
								<td class="size"></td>
								<td class="revision"></td>
								<td class="date"></td>
								<td class="author"></td>
							</tr>
						</tbody>
					</table>
			
			
				</div>
			</div>
						
						
						
			<!-- 
			<table id="tbl_userList">
				<thead>
					<tr>
						<th class="checkbox"><input type="checkbox"/></th>
						<th>ID</th>
						<th>Name</th>
						<th>Email</th>
						<th>User Authority</th>
					</tr>
				</thead>
				<tbody>
					<tr class="sample">
						<td class="checkbox"><input name="userId" type="checkbox"/></td>
						<td class="userId">
							<a href="<c:url value="/configuration/users/list/"/>">test</a>
						</td>
						<td class="userName"></td>
						<td class="email"></td>
						<td class="authType"></td>
					</tr>
				</tbody>
			</table>
			 -->
			 <!-- 
			<p>
				<a class="button green mt ml" onclick="openSearchUserDialog()"><small class="icon plus"></small><span>Add User</span></a>
				<a class="button red mt ml" onclick="delRepositoryUser()"><small class="icon cross"></small><span>Delete User</span></a>
			</p>
			 -->
		</div>
	</div>
	

	<div class="clear"></div>
</div>
