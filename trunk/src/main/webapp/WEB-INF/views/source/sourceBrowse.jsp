<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<%@ include file="/WEB-INF/views/common/include/syntaxhighlighter.jspf"%>
<script type="text/javascript">
	$(function() {
		
		listRepositorySource();
		$("#sel_repository").change(listRepositorySource);
		
	});
	
	function listRepositorySource(){
		var repositorySeq = $("#sel_repository > option:selected").val();
		$("#div_sourceTree").dynatree({
			onClick: function(node, event) {
				//if( node.isLoading ) return;
				if(node.data.isFolder) node.expand();
				retrieveSourceList(node.data.fileChildren);
				return true;
		      },
			onSelect: function(select, node) {
				//retrieveSourceList(node.getChildren());
				return true;
				if( node.isLoading ) return;
				//alert( node.data.children );
				retrieveSourceList(node.getChildren());
				//if( node.data.isFolder ) return;
				/*
				$.post("<c:url value="/source/browse/detail"/>",
						{path:node.data.path, 
							revision:node.data.revision,
							repositorySeq:repositorySeq},
			            function(data){
								var brush = getShBrush(data.path);//new SyntaxHighlighter.brushes.JScript();
					            brush.init({ toolbar: false });
					            $('#pre_fileContent').html(brush.getHtml(data.content));
			        },"json");
				*/
            },
            clickFolderMode: 1,
            selectMode: 1,
            initAjax: {
	            url: "<c:url value="/source/browse/list"/>",
	            data: {repositorySeq: repositorySeq, path:""}
	        },
			onLazyRead: function(node){
				$.getJSON(
					"<c:url value="/source/browse/list"/>",
					{repositorySeq: repositorySeq, path:node.data.path},
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
				/*
	 			node.appendAjax({
	 		    	url: "<c:url value="/source/browse/list"/>",
	 		        data: {repositorySeq: repositorySeq, path:node.data.path},
	 		        //success: function(node){
	 		        //	retrieveSourceList(node.getChildren());
	 		        //}
	 		        success: function(result, textStatus){
	 		        	if( !node.data.fileChildren ) node.data.fileChildren = [];
	 		        	alert( result );
	                    res = [];
	                    for(var inx=0, len=result.folders.length; inx<len; inx++){
	                        var source = result.folders[i];
	                        //if(source.isFolder){
	                        	res.push({title: source.title, path: source.path, isFolder:source.isFolder, isLazy: source.isLazy
     								,fileChildren:[]});
	                        //}else{
	                        //	alert(source);
	                        //	node.data.fileChildren.push(source);
	                        //}
	                        
	                    }
	                    node.setLazyNodeStatus(DTNodeStatus_Ok);
	                    node.addChild(res);
	 		        	retrieveSourceList(node.data.fileChildren);
	 		        }
	 		    });
				*/
	 		}
        });
		$("#div_sourceTree").dynatree("getTree").reload();
	};
	
	
	function retrieveSourceList(sourceNodeList){
		$("#tbl_sourceList tbody tr:not(.sample)").remove();
		if( !sourceNodeList || sourceNodeList == null ) return;
		$("#tbl_sourceList tbody tr.sample").css('display',sourceNodeList.length < 1?'inline':'');
		for( var inx = 0 ; inx < sourceNodeList.length ; inx++ ){
			var row = $("#tbl_sourceList > tbody > .sample").clone();
			$(row).children(".name").text(sourceNodeList[inx].name);
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
				<h2>Repository Tree</h2>
				<div class="desc">
					<div id="div_sourceTree" style="height:300px;" ></div>
				</div>
				<div class="bottom"><div></div></div>
			</div>
			
			<div style="float:left; width:100%;z-index:-1;">
				<div style="margin-left:320px;min-height:350px;">
					<!-- 
					<pre id="pre_fileContent" class="brush: xml">
						function test(){return true;};
					</pre>
					-->
					
					
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
								<td class="name">No files in the selected directory.</td>
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
