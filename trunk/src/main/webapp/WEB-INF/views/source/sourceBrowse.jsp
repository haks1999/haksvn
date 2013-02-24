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
			onActivate: function(node) {
				return;
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
            onSelect: function( flag, node ){
            	if( node.isLoading ) return;
            	retrieveSourceList(node.getChildren());
            },
            initAjax: {
	            url: "<c:url value="/source/browse/list"/>",
	            data: {repositorySeq: repositorySeq, path:""}
	        },
			onLazyRead: function(node){
	 			node.appendAjax({
	 		    	url: "<c:url value="/source/browse/list"/>",
	 		        data: {repositorySeq: repositorySeq, path:node.data.path},
	 		        success: function(node){
	 		        	retrieveSourceList(node.getChildren());
	 		        }
	 		        /*
	 		        success: function(data, textStatus){
	 		        	var list = data.sourceList;
	 		        	if( !node.data.isFileChildren ) node.data.isFileChildren = [];
	                    res = [];
	                    for(var inx=0, len=list.length; inx<len; inx++){
	                        var source = list[i];
	                        if(source.isFolder){
	                        	res.push({title: source.title, path: source.path, isFolder:source.isFolder, isLazy: source.isLazy
     								,isFileChildren:[]});
	                        }else{
	                        	node.data.isFileChildren.push(source);
	                        }
	                        
	                    }
	                    node.setLazyNodeStatus(DTNodeStatus_Ok);
	                    node.addChild(res);
	 		        	//retrieveSourceList(node.getChildren());
	 		        }
	 		        */

	 		    });
	 		}
        });
		$("#div_sourceTree").dynatree("getTree").reload();
	};
	
	
	function retrieveSourceList(sourceNodeList){
		$("#tbl_sourceList tbody tr:not(.sample)").remove();
		if( !sourceNodeList || sourceNodeList == null ) return;
		for( var inx = 0 ; inx < sourceNodeList.length ; inx++ ){
			var row = $("#tbl_sourceList > tbody > .sample").clone();
			$(row).children(".name").text(sourceNodeList[inx].data.name);
			$(row).children(".size").text(sourceNodeList[inx].data.size);
			$(row).children(".revision").text(sourceNodeList[inx].data.revision);
			$(row).children(".date").text(sourceNodeList[inx].data.date);
			$(row).children(".author").text(sourceNodeList[inx].data.author);
			$(row).removeClass("sample");
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
			
			<div id="div_sourceTree" style="position:absolute;display:block;width:300px;height:350px;float:left;margin-right:-370px;left:10px;"></div>
			<div style="float:left; width:100%;z-index:-1;">
				<div style="margin-left:320px">
					<!-- 
					<pre id="pre_fileContent" class="brush: xml">
						function test(){return true;};
					</pre>
					-->
					
					
					<table id="tbl_sourceList">
						<thead>
							<tr>
								<th>Filename</th>
								<th>Size</th>
								<th>Revision</th>
								<th>Date</th>
								<th>Author</th>
							</tr>
						</thead>
						<tbody>
							<tr class="sample">
								<td class="name"></td>
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
