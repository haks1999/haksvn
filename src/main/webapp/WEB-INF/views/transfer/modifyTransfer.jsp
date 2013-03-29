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

form p span font a{text-decoration:underline;cursor:pointer;}

.sourceListPanel .ui-menu { position: absolute; width: 100px; }
.sourceListPanel .ui-button .ui-button-text{font-family:Verdana,Arial,sans-serif;font-size:10px;}
.sourceListPanel .ui-button-text-only .ui-button-text{padding:.4em .4em}
.sourceListPanel .ui-button-icon-only .ui-button-text{padding:.4em .4em}
.sourceListPanel .ui-menu {z-index:1;}
.sourceListPanel .ui-menu .ui-menu-item A{font-size:10px;}

.transferSourceDetail .ui-menu { width: 120px; }
.transferSourceDetail .ui-button .ui-button-text{font-family:Verdana,Arial,sans-serif;font-size:11px;}
.transferSourceDetail .ui-button-text-only .ui-button-text{padding:.4em .4em}
.transferSourceDetail .ui-button-icon-only{width:2em;}
.transferSourceDetail .ui-button-icon-only .ui-button-text{padding:.4em .4em}
.transferSourceDetail .ui-menu {z-index:1;}
.transferSourceDetail .ui-menu .ui-menu-item A{font-size:11px;}
.transferSourceDetail table{width:auto;margin-left:25px;margin-bottom:10px;margin-top:5px;}
.transferSourceDetail table td{
border:none;
line-height:14px;
padding-top:0px;
padding-bottom:0px;
padding-left:2px;
padding-right:2px;
background-color:transparent;}
.transferSourceDetail table tr:hover td{background-color:transparent;}
.transferSourceDetail table td div.action{width:150px;height:27px;}
ul.Add li.diff{display:none;}
ul.Delete li.diff{display:none;}
ul.Delete li.revision{display:none;}
</style>
<script type="text/javascript">
	var _gRepoTrunk = '<c:out value="${repository.trunkPath}"/>';
	var _gRepoBranches = '<c:out value="${repository.branchesPath}"/>';
	
	$(function() {
		//$('#repositoryForm').validate();
		$('#frm_transfer').attr('action', '<c:url value="/transfer/request/list/${repositorySeq}/save" />');
		transformDateField();
		enableSearchSourceAutocomplete();
		if( Number('<c:out value="${transfer.transferSeq}"/>') > 0) initTransferSourceList();
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
	var _gToDelete = false;
	function openSearchSourceDialog( rootPath, toDelete ){
		_gRootPath = rootPath;
		_gToDelete = toDelete;
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
	
	function initTransferSourceList(){
		_gSourceList = [];
		$("[id^='div_transferSourceDetail_']").remove();	
		retrieveTransferSourceList();
	}
	
	
	function retrieveTransferSourceList(){
		$.getJSON(
				"<c:url value="/transfer/request/list/${repositorySeq}/${transfer.transferSeq}/sources"/>",
				{},
	            function(result){
					for( var inx = 0 ; inx < result.length ; inx++){
						addToTransferSourceList({index:inx, transferSourceSeq:result[inx].transferSourceSeq,path:result[inx].path,revision:result[inx].revision,transferSourceTypeCode:result[inx].transferSourceTypeCode,inserted:false});
					}
				});
	};
	
	var _gSourceList = [];
	function addToTransferSourceList( nSrc ){
		for( var inx = 0 ; inx < _gSourceList.length ; inx++){
			if( !_gSourceList[inx].inserted && !_gSourceList[inx].deleted && _gSourceList[inx].path == nSrc.path ) return;
		}
		if( !nSrc.index ) nSrc.index = _gSourceList.length;
		var srcDetail = $('#div_transferSourceDetail').clone().attr('id','div_transferSourceDetail_'+nSrc.index).attr('_h_index',nSrc.index);
		$(srcDetail).find('.path a').text(nSrc.path);
		$(srcDetail).find('ul').addClass(nSrc.transferSourceTypeCode.codeName);
		$('#spn_sourcesToTran').append(srcDetail);
		createTransferSourceDetailActionButton($(srcDetail).find('.transferSourceDetail'));
		$(srcDetail).find('td.type').text(nSrc.transferSourceTypeCode.codeName);
		$(srcDetail).find('td.revision').text(nSrc.revision);
		$(srcDetail).css('display','');
		_gSourceList.push(nSrc);
		transferSourceListToValue();
	};
	
	function removeFromTransferSourceList( oSrc ){
		oSrc.deleted = true;
		if(oSrc.inserted ){
			$('#div_transferSourceDetail_' + oSrc.index).remove();	
		}else{
			$('#div_transferSourceDetail_' + oSrc.index).find('.path a').css('text-decoration','line-through');
		}
		transferSourceListToValue();
	};
	
	function transferSourceListToValue(){
		var listToVal = [];
		for( var inx = 0 ; inx < _gSourceList.length ; inx++ ){
			if( _gSourceList[inx].deleted && _gSourceList[inx].inserted ) continue; 
			listToVal.push(_gSourceList[inx]);
		}
		$('#frm_transfer input[name=transferSourceList]').val(haksvn.json.stringfy(listToVal));
	}
	
	function createTransferSourceDetailActionButton( elem ){
		$(elem).find('ul li.changes').click(function(){
			var win = window.open(('<c:url value="/source/changes/${repositorySeq}" />' + '/' + _gSourceList[$(elem).parent().attr('_h_index')].path).replace("//", "/"), '_blank');
			win.focus();
		});
		$(elem).find('ul li.diff').click(function(){
			var srcD = _gSourceList[$(elem).parent().attr('_h_index')];
			var param='?repositorySeq=' + '<c:out value="${repositorySeq}"/>'
						+'&srcPath=' + _gRepoBranches+srcD.path.substr(_gRepoTrunk.length)
						+'&path=' + srcD.path
						+'&srcRev=-1&trgRev=' + srcD.revision;
			var win = window.open('<c:url value="/source/changes/diff" />' + param, '_blank');
			win.focus();
		});
		$(elem).find('ul li.remove').click(function(){
			removeFromTransferSourceList(_gSourceList[$(elem).parent().attr('_h_index')]);
		});
		$(elem).find("button.action").button().click(function() {
	        	var win = window.open(('<c:url value="/source/browse/${repositorySeq}" />' + '/' + _gSourceList[$(elem).parent().attr('_h_index')].path + '?rev=' + _gSourceList[$(elem).parent().attr('_h_index')].revision).replace("//", "/"), '_blank');
				win.focus();
				return false;
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
	
	function createSourceListActionButton( row ){
		var path = $(row).attr('path');
		var rev = $(row).attr('rev');
		$(row).find('ul li.browse').click(function(){
			var win = window.open(('<c:url value="/source/browse/${repositorySeq}" />' + '/' + path + '?rev=' + rev).replace("//", "/"), '_blank');
			win.focus();
		});
		$(row).find('ul li.changes').click(function(){
			var win = window.open(('<c:url value="/source/changes/${repositorySeq}" />' + '/' + path).replace("//", "/"), '_blank');
			win.focus();
		});
		$(row).find("td button.action").button().click(function() {
	        	haksvn.block.on();
	        	$.getJSON( "<c:url value="/transfer/request/check/${repository.repositorySeq}"/>",
      						{path:path,del:_gToDelete}, 
      						function(data){
      							if( !data.isLocked){
      								addToTransferSourceList({transferSourceSeq:0,path:path,revision:rev,transferSourceTypeCode:data.transferSourceTypeCode,inserted:true});
      								haksvn.block.off();
      								return;
      							}
      							haksvn.block.off();
      							$('#div_lockMessage .transferSeq').text('req-'+data.transfer.transferSeq);
      							$('#div_lockMessage .reuqestUserId').text(data.transfer.requestUser.userName+'('+data.transfer.requestUser.userId+')');
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
	
	
	function toggleTransferSourceDetail(pmOpenerParent){
		var pmOpener = $(pmOpenerParent).children('a.pmOpener');
		var div = $(pmOpener).parent().next('div');
		if($(pmOpener).hasClass('opened')){
			$(pmOpener).removeClass('opened').addClass('closed');
			$(div).addClass('display-none');
		}else{
			$(pmOpener).removeClass('closed').addClass('opened');
			$(div).removeClass('display-none');
			//if( !$(div).hasClass('loaded')){
			//	$(div).addClass('loading');
				//retrieveDiffWithPrevious(pmOpener, path,rev);
			//}
		}
	};
	
</script>
<div id="table" class="help">
	<h1>Transfer Information</h1>
	<div class="col w10 last">
		<div class="content">
		
			<form:form commandName="transfer" class="w200" id="frm_transfer" method="post" onsubmit="javascript:haksvn.block.on();">
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
					<span class="italic"><font class="path"><a onclick="initTransferSourceList()">(Reload)</a></font></span>
				</p>
				<p>
					<label class="left">Sources To Transfer</label>
					<span class="italic"><font class="path"><a onclick="openSearchSourceDialog('<c:out value="${repository.trunkPath}" />',false)">Add</a></font></span>
					<input type="text" class="text visible-hidden"/>
					<span id="spn_sourcesToTran" style="display:block;margin-left:220px;"></span>
				</p>
				<p>
					<label class="left">Sources To Delete</label>
					<span class="italic"><font class="path"><a onclick="openSearchSourceDialog('<c:out value="${repository.branchesPath}" />',true)" style="text-decoration:underline;cursor:pointer;">Add</a></font></span>
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



<div id="div_transferSourceDetail" style="display:none;">
	<span onclick="toggleTransferSourceDetail(this)">
		<a class="pmOpener closed">
			<img class="pClosed" src="<c:url value="/images/plus_small_white.png"/>"/><img class="mOpened" src="<c:url value="/images/minus_small_white.png"/>"/>
		</a>
		<span>
			<font class="path font12">
				<a></a>
			</font>
		</span>
	</span>
	<div class="transferSourceDetail display-none">
		<table>
			<tr>
				<td>
					<div class="action">
					  	<div>
					    	<button class="action">View Source</button>
					    	<button>Select an action</button>
					  	</div>
					  	<ul>
					  		<li class="revision"><a>Change Revision</a></li>
					    	<li class="changes"><a>View Revisions</a></li>
					    	<li class="diff"><a>Diff with Prod</a></li>
					    	<li class="remove"><a>Remove</a></li>
					  	</ul>
					</div>
				</td>
				<td class="type">
				</td>
				<td class="revision">
				</td>
				<td class="log">
				</td>
			</tr>
		</table>
	</div>
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
								    	<li class="browse"><a>View Source</a></li>
								    	<li class="changes"><a>View Revisions</a></li>
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
