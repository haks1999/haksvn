<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<style>
.sourceTreePanel{
float:left;border:#999a9b 1px solid;height:250px;
}
.sourceTreePanel > div{
height:242px;width:250px;
}
.sourceListPanel{
float:left;width:440px;height:250px;overflow:auto;margin-left:5px;
}

.sourceListPanel .ui-menu { position: absolute; width: 100px; }
.sourceListPanel .ui-button .ui-button-text{font-family:Verdana,Arial,sans-serif;font-size:10px;}
.sourceListPanel .ui-button-text-only .ui-button-text{padding:.4em .4em}
.sourceListPanel .ui-button-icon-only .ui-button-text{padding:.4em .4em}
.sourceListPanel .ui-menu {z-index:1;}
.sourceListPanel .ui-menu .ui-menu-item A{font-size:10px;}
</style>
<script type="text/javascript">
	$(function() {
		//$('#repositoryForm').validate();
		$('#frm_transfer').attr('action', '<c:url value="/transfer/request/list/${repositorySeq}/save" />');
		transformDateField();
		enableSearchSourceAutocomplete();
		if( Number('<c:out value="${transfer.transferSeq}"/>') > 0) retrieveTransferSourceList();
		$('#btn_searchSource').button().click(searchSource);
   	});
	
	function transformDateField(){
		var requestDate = Number('<c:out value="${transfer.requestDate}"/>');
		var transferDate = '<c:out value="${transfer.transferDate}"/>';
		if( requestDate > 0 ) $('#frm_transfer input.requestDate').val(haksvn.date.convertToComplexFullFormat(new Date(requestDate)));
		if( transferDate > 0 ) $('#frm_transfer input.transferDate').val(haksvn.date.convertToComplexFullFormat(new Date(transferDate)));
	};
	
	function enableSearchSourceAutocomplete(){
		$("#txt_searchSource").keypress(function(event) {
		    if (event.which == 13) {
		        event.preventDefault();
		        searchSource();
		    }
		});
		
		$( "#txt_searchSource" ).autocomplete({
			source: function( request, response ) {
						//if( autocompleteExtractLast(request.term).trim().length < 2 ) return;
		          		$.postJSON( "<c:url value="/source/browse/search/dir"/>",
		          					{repositorySeq:'<c:out value="${repository.repositorySeq}"/>',
		          						path:(function(){
		          							var searchVal = $( "#txt_searchSource" ).val();
		          							return (_gRootPath + '/' + searchVal).replace('//','/');
		          						})()}, 
		          					function(data){
			            				if (!data.length || data.length < 1) {
			            					data = [{ noresult: true, noresultmsg: "No Result!"}];
			            				}
			            				response(data);
		        	  	});
		          
		        	},
			select: function( event, ui ) {
						if(ui.item.noresultmsg) return false;
				        this.value = ui.item.path.substr(_gRootPath.length);
				        return false;
					},
			minLength: 1,
			focus: 	function( event, ui ) {
						// prevent value inserted on focus
						return false;
		      		},
			open: 	function() {
						$( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
					},
			close: 	function() {
						$( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
					}
		})
		.data( "autocomplete" )._renderItem = function( ul, item ) {
			if( item.noresult ){
				return $( "<li>" )
		        .append( "<a class=\"italic\">" + item.noresultmsg + "</a>" )
		        .appendTo( ul ); 
			}else{
				return $( "<li>" )
		        .append( "<a>" + item.path.substr(_gRootPath.length) + "</a>" )
		        .appendTo( ul );
			}
		     
		};
	};
	
	var _gRootPath = '';
	function openSearchSourceDialog( rootPath ){
		_gRootPath = rootPath;
		listRepositorySource('');
		$("#div_searchSource").dialog({
			resizable:false,
			height: 470,
		    width: 750,
		    modal: true,
		    buttons: {
		    	"Close": function() {
		    		$( "#txt_searchSource" ).val('');
		        	destroySourceTree();
		            $( this ).dialog( "close" );
		        }
		    }
	    });
		$( "#txt_searchSource" ).focus();
	};
	
	function destroySourceTree(){
		$("#div_sourceTree").dynatree("destroy");
		$("#div_sourceTree").children().remove();
	};
	
	function searchSource(){
		destroySourceTree();
		listRepositorySource($('#txt_searchSource').val());
	};
	
	function listRepositorySource( searchPath ){
		$("#div_sourceTree").dynatree({
			onClick: function(node, event) {
				if(node.data.isFolder) node.expand();
				retrieveSourceList(node.data.fileChildren);
				return true;
		      },
            clickFolderMode: 1,
            selectMode: 1,
	        children:[{title:'[SVN]',path:_gRootPath+searchPath,isLazy:true,isFolder:true, expand:true}],
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
		for( var inx = 0 ; inx < sourceNodeList.length ; inx++ ){
			var row = $("#tbl_sourceList > tbody > .sample").clone();
			$(row).attr('path',sourceNodeList[inx].path).attr('rev',sourceNodeList[inx].revision);
			createSourceListActionButton(row);
			$(row).find(".name").text(sourceNodeList[inx].name);
			$(row).removeClass("sample");
			$(row).css('display','');
			$('#tbl_sourceList > tbody').append(row);
		}
		$( "#txt_searchSource" ).focus();
	};
	
	var _gSourceList = [];
	function retrieveTransferSourceList(){
		$.getJSON(
				"<c:url value="/transfer/request/list/${repositorySeq}/${transfer.transferSeq}/sources"/>",
				{},
	            function(result){
					for( var inx = 0 ; inx < result.length ; inx++){
						addToSourceList({transferSourceSeq:result[inx].transferSourceSeq,path:result[inx].path,revision:result[inx].path});
					}
				});
	};
	
	function addToSourceList( nSrc ){
		for( var inx = 0 ; inx < _gSourceList.length ; inx++){
			if( _gSourceList[inx].path == nSrc.path ) return;
		}
		var srcDetail = $('#div_sourceDetail').clone();
		$(srcDetail).find('.path a').text(nSrc.path.substr(_gRootPath.length));
		$('#spn_sourcesToTran').append(srcDetail);
		$(srcDetail).css('display','');
		_gSourceList.push(nSrc);
		$('#frm_transfer input[name=transferSourceList]').val(haksvn.json.stringfy(_gSourceList));
	}
	
	function createSourceListActionButton( row ){
		var path = $(row).attr('path');
		var rev = $(row).attr('rev');
		$(row).find('ul li a.browse').click(function(){
			var win = window.open(('<c:url value="/source/browse/${repositorySeq}" />' + '/' + path + '?rev=' + rev).replace("//", "/"), '_blank');
			win.focus();
		});
		$(row).find('ul li a.changes').click(function(){
			var win = window.open(('<c:url value="/source/changes/${repositorySeq}" />' + '/' + path).replace("//", "/"), '_blank');
			win.focus();
		});
		$(row).find("td button.action").button().click(function() {
	        	haksvn.block.on();
	        	$.getJSON( "<c:url value="/transfer/request/lock/${repository.repositorySeq}"/>",
      						{path:path}, 
      						function(data){
      							haksvn.block.off();
      							if( data == null || !data ){
      								addToSourceList({path:path,revision:rev});
      								return;
      							}
      							$('#div_lockMessage .transferSeq').text('req-'+data.transferSeq);
      							$('#div_lockMessage .reuqestUserId').text(data.requestUser.userName+'('+data.requestUser.userId+')');
            					$( "#div_lockMessage" ).dialog({
            				      	modal: true,
            				      	title:'Locking Infomation',
            				      	resizable:false,
            				      	buttons: {
            				        	Ok: function() {
            				          		$( this ).dialog( "close" );
            				        	}
            				      	}
            				    });
    	  		});
	        	
	        	
	    	}).next().button({
				text: false,
	          	icons: {
	            	primary: "ui-icon-triangle-1-s"
	          	}
	        }).click(function() {
	        	var menu = $( this ).parent().next().show().position({
	            	my: "left top",
	            	at: "left bottom",
	            	of: this
	          	});
	          	$( document ).one( "click", function() {
	            	menu.hide();
	          	});
	          	return false;
	        }).parent().buttonset().next().hide().menu();
	};
	
</script>
<div id="table" class="help">
	<h1>Transfer Information</h1>
	<div class="col w10 last">
		<div class="content">
		
			<form:form commandName="transfer" class="w200" id="frm_transfer" method="post">
				<p><span class="strong">Detail</span></p>
				<p>
					<form:label path="transferSeq" class="left">Transfer Seq</form:label>
					<form:input class="text w_20 readOnly ${transfer.transferSeq < 1?'visible-hidden':''}" path="transferSeq" readonly="true"/>
				</p>
				<p>
					<form:label path="repositorySeq" class="left">Repository</form:label>
					<form:select path="repositorySeq" disabled="true" items="${repositoryList}" itemValue="repositorySeq" itemLabel="repositoryName"/>
				</p>
				<p>
					<form:label path="transferTypeCode.codeId" class="left">Type</form:label>
					<form:select path="transferTypeCode.codeId" disabled="${not transferStateAuth.isEditable}" items="${requestScope['transfer.type.code']}" itemValue="codeId" itemLabel="codeName"/>
				</p>
				<p>
					<form:label path="transferStateCode.codeId" class="left">State</form:label>
					<form:select path="transferStateCode.codeId" disabled="true" items="${requestScope['transfer.state.code']}" itemValue="codeId" itemLabel="codeName"/>
				</p>
				<p>
					<form:label path="description" class="left">Description</form:label>
					<form:textarea class="text" disabled="${not transferStateAuth.isEditable}" cols="50" rows="5" path="description"/>
					<form:errors path="description" />
				</p>
				<p>
					<form:hidden path="requestUser.userId" />
					<form:hidden path="requestUser.userName" />
					<label class="left">Request User</label>
					<input type="text" class="text w_30 readOnly" readonly value="${transfer.requestUser.userName}(${transfer.requestUser.userId})"/>
				</p>
				<p>
					<form:hidden path="requestDate" />
					<label class="left">Request Date</label>
					<input type="text" class="text w_30 readOnly requestDate" readonly/>
				</p>
				<p>
					<label class="left">Transfer User</label>
					<input type="text" class="text w_30 readOnly" readonly value="${transfer.transferUser.userName}(${transfer.transferUser.userId})"/>
				</p>
				<p>
					<form:hidden path="transferDate" />
					<label class="left">Transfer Date</label>
					<input type="text" class="text w_30 readOnly transferDate" readonly/>
				</p>
				<hr/>
				<input type="hidden" name="transferSourceList" />
				<p>
					<span class="strong">Sources</span>
				</p>
				<p>
					<label class="left">Sources To Transfer</label>
					<span><font class="path"><a onclick="openSearchSourceDialog('<c:out value="${repository.trunkPath}" />')" style="text-decoration:underline;cursor:pointer;">Add</a></font></span>
					<input type="text" class="text visible-hidden"/>
					<span id="spn_sourcesToTran" style="display:block;margin-left:220px;"></span>
				</p>
				<p>
					<label class="left">Sources To Delete</label>
					<span><font class="path"><a onclick="openSearchSourceDialog('<c:out value="${repository.branchesPath}" />')" style="text-decoration:underline;cursor:pointer;">Add</a></font></span>
					<input type="text" class="text visible-hidden"/>
				</p>
				<p>
					<label class="left"></label>
					<c:if test="${transferStateAuth.isRequestable}">
						<a class="button green mt ml" onclick="requestTransfer()"><small class="icon plus"></small><span>Request</span></a>
						<a class="button red mt ml" onclick="deleteTransfer()"><small class="icon cross"></small><span>Delete</span></a>
						<script type="text/javascript" >
							function requestTransfer(){
								$('#frm_transfer').attr('action', '<c:url value="/transfer/request/list" />' + '<c:out value="/${repositorySeq}/request"/>');
								$('#frm_transfer').submit();
							};
							
							function deleteTransfer(){
								$('#frm_transfer').attr('action', '<c:url value="/transfer/request/list" />' + '<c:out value="/${repositorySeq}/delete"/>');
								$('#frm_transfer').submit();
							};
						</script>
					</c:if>
					<c:if test="${transferStateAuth.isApprovable}">
						<a class="button green mt ml" onclick="approveTransfer()"><small class="icon check"></small><span>Approve</span></a>
						<a class="button red mt ml" onclick="rejectTransfer()"><small class="icon cross"></small><span>Reject</span></a>
						<script type="text/javascript" >
							function approveTransfer(){
								$('#frm_transfer').attr('action', '<c:url value="/transfer/request/list" />' + '<c:out value="/${repositorySeq}/approve"/>');
								$('#frm_transfer').submit();
							};
							
							function rejectTransfer(){
								$('#frm_transfer').attr('action', '<c:url value="/transfer/request/list" />' + '<c:out value="/${repositorySeq}/reject"/>');
								$('#frm_transfer').submit();
							};
						</script>
					</c:if>
					<c:if test="${transferStateAuth.isRequestCancelable}">
						<a class="button red mt ml" onclick="requestCancelTransfer()"><small class="icon minus"></small><span>Cancel Request</span></a>
						<script type="text/javascript" >
							function requestCancelTransfer(){
								$('#frm_transfer').attr('action', '<c:url value="/transfer/request/list" />' + '<c:out value="/${repositorySeq}/requestCancel"/>');
								$('#frm_transfer').submit();
							};
						</script>
					</c:if>
					<c:if test="${transferStateAuth.isEditable}">
						<a class="button green mt ml form_submit"><small class="icon save"></small><span>Save</span></a>
					</c:if>
					<a class="button yellow mt ml" onclick="history.back()"><small class="icon play"></small><span>Back to List</span></a>
				</p>
			</form:form>
		</div>
	</div>
	<div class="clear"></div>
</div>



<div id="div_sourceDetail" style="display:none;">
	<a class="pmOpener closed">
		<img class="pClosed" src="<c:url value="/images/plus_small_white.png"/>"/><img class="mOpened" src="<c:url value="/images/minus_small_white.png"/>"/>
	</a>
	<span>
		<font class="path font12">
			<a></a>
		</font>
	</span>
</div>

<div id="div_searchSource" title="Search Source" style="display:none;">
	<div class="module text">
	
		<div>
			<div class="box">
				<div class="head"><div></div></div>
				<div class="desc">
					<p>
						<label>Path</label> 
						<input id="txt_searchSource" class="text w_40" type="text" />
						<button id="btn_searchSource">Search</button>
					</p>
				</div>
				<div class="bottom"><div></div></div>
			</div>
		</div>
		
		<div>
	
	  		<div class="sourceTreePanel">
	  			<div id="div_sourceTree"></div>
	  		</div>
	  		
	  		<div class="sourceListPanel">
	  			<table id="tbl_sourceList" class="compact">
	  				<thead>
	  					<tr>
	  						<th class="w_130">Action</th>
	  						<th>File Name</th>
	  					</tr>
	  				</thead>
					<tbody>
						<tr class="sample">
							<td>
								<div>
								  	<div>
								    	<button class="action">Add To Req</button>
								    	<button>Select an action</button>
								  	</div>
								  	<ul>
								    	<li><a class="browse">View Source</a></li>
								    	<li><a class="changes">View Revisions</a></li>
								    	<li><a class="diff">Diff with Prod</a></li>
								  	</ul>
								</div>
							</td>
							<td class="name"></td>
						</tr>
						<tr class="nodata">
							<td colspan="2" style="display:table-cell;">No files in the selected directory.</td>
						</tr>
					</tbody>
				</table>
	  		</div>
  		
		</div>
	</div>
</div>

<div id="div_lockMessage" style="display:none;">
	<p>
    	<span class="ui-icon ui-icon-circle-check" style="float: left; margin: 0 7px 50px 0;"></span>
    	<span>This File is Locked By <b class="transferSeq"></b></span>
  	</p>
 	<p>
  		<span>Request user: </span><b class="reuqestUserId"></b>
  	</p>
</div>
