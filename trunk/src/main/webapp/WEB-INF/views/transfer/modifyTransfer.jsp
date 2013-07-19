<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<style>
.sourceTreePanel{
float:left;border:#999a9b 1px solid;height:250px;width:250px;
}
.sourceTreePanel #div_sourceTree{
height:242px;
}
.sourceListPanel{
float:left;width:440px;height:250px;overflow:auto;margin-left:5px;
}
.changeListPanel{
height:300px;overflow-x:hidden;overflow-y:auto;
}

form p span font a{text-decoration:underline;cursor:pointer;}

.sourceListPanel .ui-menu { position: absolute; width: 100px; }
.sourceListPanel .ui-button .ui-button-text{font-size:10px;}
.sourceListPanel .ui-button-text-only .ui-button-text{padding:.4em .4em}
.sourceListPanel .ui-button-icon-only .ui-button-text{padding:.4em .4em}
.sourceListPanel .ui-menu {z-index:1;}
.sourceListPanel .ui-menu .ui-menu-item A{font-size:10px;}

.changeListPanel .ui-menu { position: absolute; width: 100px; }
.changeListPanel .ui-button .ui-button-text{font-size:10px;}
.changeListPanel .ui-button-text-only .ui-button-text{padding:.4em .4em}
.changeListPanel .ui-button-icon-only .ui-button-text{padding:.4em .4em}
.changeListPanel .ui-menu {z-index:1;}
.changeListPanel .ui-menu .ui-menu-item A{font-size:10px;}

.transferSourceDetailPanel span.type{display:inline-block;width:45px;}
.transferSourceDetailPanel span.src-header{display:inline-block;width:105px;margin-right:5px;}

.transferSourceDetail {margin-left:20px;margin-top:5px;width:auto;}
.transferSourceDetail .change-info{min-height:20px;}
.transferSourceDetail.Add input.diff-prod{display:none;}
.transferSourceDetail.Delete input.chg-revision{display:none;}
.transferSourceDetail.Delete input.diff-prod{display:none;}

#tbl_changeList .revision{color:#800000;font-size:12px;text-align:center;}
#tbl_changeList .message{font-family:"Malgun Gothic";font-size:10px;}
</style>
<script type="text/javascript">
	var _gRepoTrunk = '<c:out value="${repository.trunkPath}"/>';
	var _gRepoBranches = '<c:out value="${repository.branchesPath}"/>';
	
	$(function() {
		$('#frm_transfer').attr('action', '<c:url value="/transfer/request/list/${repositoryKey}/save" />');
		transformDateField();
		enableSearchSourceAutocomplete();
		if( Number('<c:out value="${transfer.transferSeq}"/>') > 0) initTransferSourceList();
		$('#btn_searchSource').button().click(searchSource);
		setFormValidation();
   	});
	
	var validTransferForm;
	function setFormValidation(){
		validTransferForm = $("#frm_transfer").validate({
			rules: {
				description: {
					required: true,
					minlength: 10,
					maxlength: 2000
				},
				sourceListCount: {
					required: true,
					minSelect: 1
				}
			},
			submitHandler : function(form){
				// 이놈은 왜 같이 안 될까...
				//if( !validTransferForm.element("#frm_transfer input[name=sourceListCount]")) return false;
				if( !validTransferForm.valid() ) return false;
				haksvn.block.on();
				form.submit();
			}
		});
	};
	
	function transformDateField(){
		var requestDate = Number('<c:out value="${transfer.requestDate}"/>');
		var approveDate = Number('<c:out value="${transfer.approveDate}"/>');
		if( requestDate > 0 ) $('#frm_transfer input.requestDate').val(haksvn.date.convertToComplexFullFormat(new Date(requestDate)));
		if( approveDate > 0 ) $('#frm_transfer input.approveDate').val(haksvn.date.convertToComplexFullFormat(new Date(approveDate)));
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
		          					{repositoryKey:'<c:out value="${repository.repositoryKey}"/>',
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
	
	function openSearchSourceByNameDialog( rootPath, toDelete ){
		_gRootPath = rootPath;
		_gToDelete = toDelete;
		setResizableSourceTreePanel();
		listRepositorySource('');
		$("#div_searchSourceByName").dialog({
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
	
	function setResizableSourceTreePanel(){
		$("#div_sourceTreePanel").resizable({
	        minWidth: '250',
	        maxWidth: '400',
	        minHeight:'250',
	        maxHeight:'250',
	        resize: function(event, ui) {
	        	$(this).next(".sourceListPanel").width(440 + 240 - $(this).width());
	        }
	    });	
	};
	
	function destroySourceTree(){
		$("#div_sourceTree").dynatree("destroy");
		$("#div_sourceTree").children().remove();
		$("#div_sourceTreePanel").resizable("destroy");
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
		$("#tbl_sourceListByName tbody tr").not(".nodata").not(".sample").remove();
		if( !sourceNodeList || sourceNodeList == null ) return;
		$("#tbl_sourceListByName tbody tr[class~=nodata]").css('display',sourceNodeList.length < 1?'table-row':'none');
		for( var inx = 0 ; inx < sourceNodeList.length ; inx++ ){
			var row = $("#tbl_sourceListByName > tbody > .sample").clone();
			$(row).attr('path',sourceNodeList[inx].path).attr('rev',sourceNodeList[inx].revision);
			createSourceListActionButton(row);
			$(row).find(".name").text(sourceNodeList[inx].name);
			$(row).removeClass("sample");
			$(row).css('display','');
			$('#tbl_sourceListByName > tbody').append(row);
		}
		$( "#txt_searchSource" ).focus();
	};
	
	function openSearchSourceByRevisionDialog( rootPath ){
		initRepositoryChangeListByRevision();
		$("#div_searchSourceByRevision").dialog({
			resizable:false,
			height: 470,
		    width: 750,
		    modal: true,
		    buttons: {
		    	"Close": function() {
		            $( this ).dialog( "close" );
		        }
		    }
	    });
		retrieveRepositoryChangeListByRevision( rootPath );
	};

	
	
	
	function initRepositoryChangeListByRevision(){
		_gChangeByRevisionPaging.start=-1;
		_gChangeByRevisionPaging.end=-1;
		$("#tbl_changeListByRevision tbody tr:not(.sample)").remove();
		$("#tbl_changeListByRevision tfoot").removeClass('display-none');
	};
	
	var _gChangeByRevisionPaging = {start:-1,end:-1,limit:10,repositoryKey:'<c:out value="${repositoryKey}"/>'};
	function retrieveRepositoryChangeListByRevision( oPath ){
		$("#tbl_changeListByRevision tfoot span:not(.loader)").removeClass('display-none').addClass('display-none');
		$("#tbl_changeListByRevision tfoot span.loader").removeClass('display-none');
		_gChangeByRevisionPaging.path = oPath;
		$.getJSON( "<c:url value="/source/changes/list"/>",
				_gChangeByRevisionPaging,
				function(data) {
					var model = data.model;
					_gChangeByRevisionPaging.start = data.end;
					for( var inx = 0 ; inx < model.length ; inx++ ){
						var row = $("#tbl_changeListByRevision > tbody > .sample").clone();
						//$(row).attr('rev',model[inx].revision);
						$(row).find(".revision").text('r'+model[inx].revision);
						//$(row).children(".message").text(model[inx].message);
						$(row).children(".date").text(haksvn.date.convertToEasyFormat(new Date(model[inx].date)));
						$(row).children(".author").text(model[inx].author);
						$(row).removeClass("sample");
						//createRepositoryChangeListActionButton(row, oPath);
						$('#tbl_changeListByRevision > tbody').append(row);
					}
					
					if( model.length < 1 ){
						$("#tbl_changeListByRevision tfoot span:not(.nodata)").removeClass('display-none').addClass('display-none');
						$("#tbl_changeListByRevision tfoot span.nodata").removeClass('display-none');
					}else if( !data.hasNext ){
						$("#tbl_changeListByRevision tfoot").removeClass('display-none').addClass('display-none');
					}else{
						$("#tbl_changeListByRevision tfoot span:not(.showmore)").removeClass('display-none').addClass('display-none');
						$("#tbl_changeListByRevision tfoot span.showmore").removeClass('display-none');
					}
		});
	};
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	function initTransferSourceList(){
		$("[id^='div_transferSourceDetail_']").remove();	
		retrieveTransferSourceList();
	}
	
	var _gTransferChanged = false;
	function retrieveTransferSourceList(){
		haksvn.block.on();
		$.getJSON(
				"<c:url value="/transfer/request/list/${repositoryKey}/${transfer.transferSeq}/sources"/>",
				{},
	            function(result){
					for( var inx = 0 ; inx < result.length ; inx++){
						addToTransferSourceList(result[inx]);
					}
					_gTransferChanged = false;
					haksvn.block.off();
				});
	};
	
	function addToTransferSourceList( nSrc ){
		if( $('#' + transformPathToId(nSrc.path)).length > 0 ){
			$( "#div_duplicateMessage" ).find(".path").text(nSrc.path);
			$( "#div_duplicateMessage" ).dialog({
		      	modal: true,
		      	title:'Duplicate Source',
		      	resizable:false,
		      	buttons: {
		        	Ok: function() {
		          		$( this ).dialog( "close" );
		        	}
		      	}
		    });
			return;
		}
		var srcDetail = $('#div_transferSourceDetail').clone().attr('id',transformPathToId(nSrc.path)).data('transferSource',nSrc);
		$(srcDetail).find(".transferSourceDetail").addClass(nSrc.transferSourceTypeCode.codeName);
		$('#spn_sourcesToTran').append(srcDetail);
		initTransferSourceDetailButtons(srcDetail);
		initTransferSourceDetailLinks( nSrc.path, nSrc.revision);
		$(srcDetail).find('.src-header span.type').text(nSrc.transferSourceTypeCode.codeName);
		$(srcDetail).removeClass('display-none');
		changeCurrentSourceListCount(1);
	};
	
	function changeCurrentSourceListCount(change){
		var curCnt = $("#frm_transfer input[name=\"sourceListCount\"]").val();
		$("#frm_transfer input[name=\"sourceListCount\"]").val(Number(curCnt) + change).change();
	};
	
	function transformPathToId( oPath ){
		return oPath.split('_').join('-').split('/').join('_').split('.').join('__');
	};
	
	function initTransferSourceDetailButtons( oElem ){
		$(oElem).find("input").attr("disabled","disabled");
		$(oElem).find("input.remove").unbind("click").click(function(){
			if( $(this).hasClass("removed") ){
				$(this).parent().children("input").removeAttr("disabled");
				$(oElem).data("transferSource").deleted = false;
				$(oElem).find('.path').removeClass('strike');
				$(this).removeClass("removed").attr("value","Remove");
				changeCurrentSourceListCount(1);
			}else{
				$(this).parent().children("input").not(".remove").attr("disabled","disabled");
				$(oElem).data("transferSource").deleted = true;
				$(oElem).find('.path').addClass('strike');
				$(this).addClass("removed").attr("value","Remove Cancel");
				changeCurrentSourceListCount(-1);
			}
		});
		$(oElem).find("input.diff-prod").unbind("click").click(function(){
			var srcD = $(oElem).data('transferSource');
			var param='?repositoryKey=' + '<c:out value="${repositoryKey}"/>'
						+'&srcPath=' + srcD.path
						+'&path=' + _gRepoBranches+srcD.path.substr(_gRepoTrunk.length)
						+'&srcRev=-1&trgRev=' + srcD.revision;
			var win = window.open('<c:url value="/source/changes/diff" />' + param, '_blank');
			win.focus();
		});
		$(oElem).find("input.chg-revision").unbind("click").click(function(){
			_gChangePaging.path = $(oElem).data('transferSource').path;
			openRevisionSourceDialog(_gChangePaging.path);
		});
	};
	
	function initTransferSourceDetailLinks( oPath, rev ){
		var srcDetail = $('#' + transformPathToId(oPath));
		$(srcDetail).find(".change-info").removeClass("loaded").children("div").not(".sample").remove();
		$(srcDetail).find('.src-tail font.path a').text(oPath);
		$(srcDetail).find('.src-tail a').attr("href",('<c:url value="/source/browse/${repositoryKey}/" />' + oPath + '?rev=' + rev).replace("//", "/"));
		$(srcDetail).find('.src-header font.revision a').text('r' + rev);
		$(srcDetail).find('.src-header a').attr("href",'<c:url value="/source/changes/${repositoryKey}" />' + '?rev=' + rev);
	};
	
	function createSourceListActionButton( row ){
		var path = $(row).attr('path');
		var rev = $(row).attr('rev');
		$(row).find('ul li.browse').click(function(){
			var win = window.open(('<c:url value="/source/browse/${repositoryKey}" />' + '/' + path + '?rev=' + rev).replace("//", "/"), '_blank');
			win.focus();
		});
		$(row).find('ul li.changes').click(function(){
			var win = window.open(('<c:url value="/source/changes/${repositoryKey}" />' + '/' + path).replace("//", "/"), '_blank');
			win.focus();
		});
		$(row).find("td button.action").button().click(function() {
	        	haksvn.block.on();
	        	$.getJSON( "<c:url value="/transfer/request/list/check/${repository.repositoryKey}"/>",
      						{path:path,del:_gToDelete}, 
      						function(data){
      							if( !data.isLocked){
      								data.transferSourceSeq = 0;
      								data.revision = rev;
      								addToTransferSourceList(data);
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
			
			var loadingElem = $(pmOpenerParent).parent().find(".change-info");
			if( !$(loadingElem).hasClass('loaded')){
				$(loadingElem).addClass('loading');
				retrieveChangeInfo(loadingElem, $(pmOpenerParent).parent().data("transferSource"));
			}
		}
	};
	
	function retrieveChangeInfo(loadingElem, oSrc){
		$.getJSON(
				"<c:url value="/source/changes/search"/>",
				{
					repositoryKey: "<c:out value="${repositoryKey}"/>",
					path: oSrc.path,
					rev: oSrc.revision
				},
	            function(result){
					$(loadingElem).removeClass('loading').addClass('loaded');
					var infoElem = $(loadingElem).find(".sample").clone().removeClass("sample display-none");
					$(infoElem).find("pre.message").text(result.log.message);
					$(infoElem).find("b.author").text(result.log.author);
					$(infoElem).find("b.date").text(haksvn.date.convertToComplexFullFormat(new Date(result.log.date)));
					$(loadingElem).append(infoElem);
					$(loadingElem).next().find("input").removeAttr("disabled");
				});
	};
	
	function openRevisionSourceDialog( oPath ){
		initRepositoryChangeList();
		$("#div_searchRevision").dialog({
			resizable:false,
			height: 450,
		    width: 600,
		    modal: true,
		    buttons: {
		    	"Close": function() {
		            $( this ).dialog( "close" );
		        }
		    }
	    });
		retrieveRepositoryChangeList( oPath );
	};
	
	function initRepositoryChangeList(){
		_gChangePaging.start=-1;
		_gChangePaging.end=-1;
		$("#tbl_changeList tbody tr:not(.sample)").remove();
		$("#tbl_changeList tfoot").removeClass('display-none');
	};
	
	var _gChangePaging = {start:-1,end:-1,limit:50,repositoryKey:'<c:out value="${repositoryKey}"/>'};
	function retrieveRepositoryChangeList( oPath ){
		$("#tbl_changeList tfoot span:not(.loader)").removeClass('display-none').addClass('display-none');
		$("#tbl_changeList tfoot span.loader").removeClass('display-none');
		$.getJSON( "<c:url value="/source/changes/list"/>",
				_gChangePaging,
				function(data) {
					var model = data.model;
					_gChangePaging.start = data.end;
					for( var inx = 0 ; inx < model.length ; inx++ ){
						var row = $("#tbl_changeList > tbody > .sample").clone();
						$(row).attr('rev',model[inx].revision);
						$(row).find(".revision").text('r'+model[inx].revision);
						$(row).children(".message").text(model[inx].message);
						$(row).children(".date").text(haksvn.date.convertToEasyFormat(new Date(model[inx].date)));
						$(row).children(".author").text(model[inx].author);
						$(row).removeClass("sample");
						createRepositoryChangeListActionButton(row, oPath);
						$('#tbl_changeList > tbody').append(row);
					}
					
					if( model.length < 1 ){
						$("#tbl_changeList tfoot span:not(.nodata)").removeClass('display-none').addClass('display-none');
						$("#tbl_changeList tfoot span.nodata").removeClass('display-none');
					}else if( !data.hasNext ){
						$("#tbl_changeList tfoot").removeClass('display-none').addClass('display-none');
					}else{
						$("#tbl_changeList tfoot span:not(.showmore)").removeClass('display-none').addClass('display-none');
						$("#tbl_changeList tfoot span.showmore").removeClass('display-none');
					}
		});
	};
	
	function createRepositoryChangeListActionButton( row, oPath ){
		var rev = $(row).attr('rev');
		$(row).find('ul li.browse').click(function(){
			var win = window.open(('<c:url value="/source/browse/${repositoryKey}" />' + '/' + oPath + '?rev=' + rev).replace("//", "/"), '_blank');
			win.focus();
		});
		$(row).find("td button.action").button().click(function() {
				$("#"+transformPathToId(oPath)).data("transferSource").revision = rev;
				initTransferSourceDetailLinks( oPath, rev );
				initTransferSourceDetailButtons( $("#"+transformPathToId(oPath)) );
				var loadingElem = $("#"+transformPathToId(oPath)).find(".change-info").addClass("loading");
				retrieveChangeInfo(loadingElem, $("#"+transformPathToId(oPath)).data("transferSource"));
				$('#div_searchRevision').dialog("close");
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
	
	function expandAllTransferDetail(){
		$('#frm_transfer .transferSourceDetailPanel .pmOpener.closed').each(function(){
			toggleTransferSourceDetail($(this).parent());
		});
	};
	
	function collapseAllTransferDetail(){
		$('#frm_transfer .transferSourceDetailPanel .pmOpener.opened').parent().trigger('click');
	};
	
</script>
<div class="content-page">
	<h1>Transfer Information</h1>
	<div class="col w10 last">
		<div class="content">
		
			<form:form commandName="transfer" class="w200" id="frm_transfer" method="post" >
				<p><span class="strong">Detail</span></p>
				<p>
					<form:label path="transferSeq" class="left">Transfer Seq</form:label>
					<form:input class="text w_20 readOnly ${transfer.transferSeq < 1?'visible-hidden':''}" path="transferSeq" readonly="true"/>
				</p>
				<p>
					<form:label path="repositoryKey" class="left">Repository</form:label>
					<form:select path="repositoryKey"  disabled="true" items="${repositoryList}" itemValue="repositoryKey" itemLabel="repositoryName"/>
				</p>
				<p>
					<form:label path="transferTypeCode.codeId" class="left">Type</form:label>
					<form:select path="transferTypeCode.codeId" disabled="${not transferStateAuth.isEditable}" items="${requestScope['transfer.type.code']}" itemValue="codeId" itemLabel="codeName"/>
					<span class="form-help"><spring:message htmlEscape="true" code="helper.transfer.type" /></span>
				</p>
				<p>
					<form:label path="transferStateCode.codeId" class="left">State</form:label>
					<form:select path="transferStateCode.codeId" disabled="true" items="${requestScope['transfer.state.code']}" itemValue="codeId" itemLabel="codeName"/>
					<span class="form-help"><spring:message htmlEscape="true" code="helper.transfer.state" /></span>
				</p>
				<p>
					<form:label path="description" class="left">Description</form:label>
					<form:textarea class="text" disabled="${not transferStateAuth.isEditable}" cols="50" rows="5" path="description"/>
					<form:errors path="description" />
					<span class="form-help"><spring:message htmlEscape="true" code="helper.transfer.description" /></span>
					<span class="form-status"></span>
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
					<label class="left">Approve User</label>
					<input type="text" class="text w_30 readOnly" readonly value="${transfer.approveUser.userName}(${transfer.approveUser.userId})"/>
				</p>
				<p>
					<form:hidden path="approveDate" />
					<label class="left">Approve Date</label>
					<input type="text" class="text w_30 readOnly approveDate" readonly/>
				</p>
				<p>
					<form:hidden path="transferGroup.transferGroupSeq" />
					<label class="left">Transfer Group</label>
					<c:if test="${not empty transfer.transferGroup && transfer.transferGroup.transferGroupSeq > 0}">
						<font class="path open-window"><a href="<c:url value="/transfer/requestGroup/list/${repository.repositoryKey}/${transfer.transferGroup.transferGroupSeq}"/>"><c:out value="group-${transfer.transferGroup.transferGroupSeq}"/></a></font>
					</c:if>
					<input type="text" class="text visible-hidden"/>
				</p>
				<p>
					<form:hidden path="revision" />
					<label class="left">Commit revision</label>
					<c:if test="${transfer.revision > 0 }">
						<font class="path open-window"><a href="<c:url value="/source/changes/${repository.repositoryKey}?rev=${transfer.revision}"/>"><c:out value="r${transfer.revision}"/></a></font>
					</c:if>
					<input type="text" class="text visible-hidden"/>
				</p>
				<hr/>
				<p>
					<span class="strong">Sources</span>
				</p>
				<p>
					<label class="left">Sources To Transfer</label>
					<span class="italic"><font class="path"><a onclick="expandAllTransferDetail()">expand all</a></font></span>
					<span class="italic"><font class="path"><a onclick="collapseAllTransferDetail()">collapse all</a></font></span>
					<input type="text" name="sourceListCount" readonly class="text readOnly w_10" style="text-align:right;" value="${fn:length(transfer.sourceList)}"/> sources
					<span class="form-status"></span>
					<span style="display:block;margin-left:210px;">
						<c:if test="${transferStateAuth.isEditable}">
							<span style="display:block;">
								<select style="border-width:1px;padding:0px;">
									<option value="openSearchSourceByNameDialog('<c:out value="${repository.trunkPath}" />',false)">transfer to production, by name</option>
									<option value="openSearchSourceByRevisionDialog('<c:out value="${repository.trunkPath}" />')">transfer to production, by revision</option>
									<option value="openSearchSourceByNameDialog('<c:out value="${repository.branchesPath}" />',true)">delete from production, by name</option>
								</select>
								<input type="button" value="Search & Add" onclick="eval($(this).parent().children('select').val())"/>
							</span>
						</c:if>
						<span id="spn_sourcesToTran"></span>
					</span>
				</p>
				<p>
					<label class="left"></label>
					<c:if test="${transferStateAuth.isRequestable}">
						<a class="button green mt ml" onclick="requestTransfer()"><small class="icon plus"></small><span>Request</span></a>
						<a class="button red mt ml" onclick="deleteTransfer()"><small class="icon cross"></small><span>Delete</span></a>
						<script type="text/javascript" >
							$(function() {
								$("#frm_transfer textarea[name='description']").change(function(){
									_gTransferChanged = true;
								});
								$("#frm_transfer input[name='sourceListCount']").change(function(){
									_gTransferChanged = true;
								});
							});
							
							function requestTransfer(){
								if( _gTransferChanged ){
									$("#div_changeOccurMessage").dialog({
										title:'Changes occured',
								      	resizable:false,
								      	width:300,
									    modal: true,
									    buttons: {
									    	"Yes": function(){
									    		$('#frm_transfer').attr('action', '<c:url value="/transfer/request/list" />' + '<c:out value="/${repositoryKey}/request"/>');
												$('#frm_transfer').submit();
										    },
									    	"No": function() {
									            $( this ).dialog( "close" );
									            return;
									        }
									    }
								    });
								}else{
									$('#frm_transfer').attr('action', '<c:url value="/transfer/request/list" />' + '<c:out value="/${repositoryKey}/request"/>');
									$('#frm_transfer').submit();
								}
							};
							
							function deleteTransfer(){
								$('#frm_transfer').attr('action', '<c:url value="/transfer/request/list" />' + '<c:out value="/${repositoryKey}/delete"/>');
								$('#frm_transfer').submit();
							};
						</script>
					</c:if>
					<c:if test="${transferStateAuth.isApprovable}">
						<a class="button green mt ml" onclick="approveTransfer()"><small class="icon check"></small><span>Approve</span></a>
						<a class="button red mt ml" onclick="rejectTransfer()"><small class="icon cross"></small><span>Reject</span></a>
						<script type="text/javascript" >
							function approveTransfer(){
								var isEmergencyType = $('#frm_transfer select[name="transferTypeCode.codeId"]').val() == "<spring:eval expression="T(com.haks.haksvn.common.code.util.CodeUtils).getTransferEmergencyTypeCodeId()"/>";
								var callApprove = function(){
									haksvn.block.on();
									var queryString = $('#frm_transfer').serialize();
									$.post('<c:url value="/transfer/request/list" />' + '<c:out value="/${repositoryKey}/approve"/>',
										queryString,
							            function(data){
											haksvn.block.off();
											if( data.type != 'success'){
												$().Message({type:data.type,text:data.text});
												return;
											}
											if( isEmergencyType ){
												var sCode = "<spring:eval expression="T(com.haks.haksvn.common.code.util.CodeUtils).getTransferTransferedCodeId()"/>";
												location.href = "<c:url value="/transfer/requestGroup/list/${repositoryKey}"/>" + "?sCode=" + sCode;
												return;
											}
											$("#div_approveSuccessMessage").dialog({
												title:'Approved Request',
										      	resizable:false,
										      	width:300,
											    modal: true,
											    buttons: {
											    	"Yes": function(){
											    		var sCode = "<spring:eval expression="T(com.haks.haksvn.common.code.util.CodeUtils).getTransferGroupStandbyCodeId()"/>";
											    		location.href = "<c:url value="/transfer/requestGroup/list/${repositoryKey}"/>" + "?sCode=" + sCode;
												    },
											    	"No": function() {
											    		var sCode = "<spring:eval expression="T(com.haks.haksvn.common.code.util.CodeUtils).getTransferApprovedCodeId()"/>";
											    		location.href = "<c:url value="/transfer/request/list/${repositoryKey}"/>" + "?sCode=" + sCode;
											        }
											    }
										    });
											
							        },"json");
								};
								
								if(isEmergencyType){
									$("#div_approveEmergencyMessage").dialog({
										title:'Approved Emergency Request',
								      	resizable:false,
								      	width:400,
									    modal: true,
									    buttons: {
									    	"Yes": function(){
									    		callApprove();
										    },
									    	"No": function() {
									    		$( this ).dialog( "close" );
									        }
									    }
								    });
								}else{
									callApprove();
								}
							};
							
							function rejectTransfer(){
								$('#frm_transfer').attr('action', '<c:url value="/transfer/request/list" />' + '<c:out value="/${repositoryKey}/reject"/>');
								$('#frm_transfer').submit();
							};
						</script>
					</c:if>
					<c:if test="${transferStateAuth.isRequestCancelable}">
						<a class="button red mt ml" onclick="requestCancelTransfer()"><small class="icon minus"></small><span>Cancel Request</span></a>
						<script type="text/javascript" >
							function requestCancelTransfer(){
								$('#frm_transfer').attr('action', '<c:url value="/transfer/request/list" />' + '<c:out value="/${repositoryKey}/requestCancel"/>');
								$('#frm_transfer').submit();
							};
						</script>
					</c:if>
					<c:if test="${transferStateAuth.isEditable}">
						<a class="button green mt ml" onclick="saveTransfer()"><small class="icon save"></small><span>Save</span></a>
						<script type="text/javascript" >
							function saveTransfer(){
								
								$("#frm_transfer input[name^='sourceList[']").remove();
								var sourceCnt = 0;
								$("#spn_sourcesToTran .transferSourceDetailPanel").each(function(){
									var transferSource = $(this).data("transferSource"); 
									if( transferSource.deleted && transferSource.transferSourceSeq < 1 ) return;
									$('#frm_transfer').append("<input type=\"hidden\" name=\"sourceList[" + sourceCnt + "].transfer.transferSeq\" value=\"" + "<c:out value="${transfer.transferSeq}"/>" + "\" />");
									$('#frm_transfer').append("<input type=\"hidden\" name=\"sourceList[" + sourceCnt + "].path\" value=\"" + transferSource.path + "\" />");
									$('#frm_transfer').append("<input type=\"hidden\" name=\"sourceList[" + sourceCnt + "].revision\" value=\"" + transferSource.revision + "\" />");
									$('#frm_transfer').append("<input type=\"hidden\" name=\"sourceList[" + sourceCnt + "].transferSourceSeq\" value=\"" + transferSource.transferSourceSeq + "\" />");
									$('#frm_transfer').append("<input type=\"hidden\" name=\"sourceList[" + sourceCnt + "].transferSourceTypeCode.codeId\" value=\"" + transferSource.transferSourceTypeCode.codeId + "\" />");
									if( transferSource.deleted === true ){
										$('#frm_transfer').append("<input type=\"hidden\" name=\"sourceList[" + sourceCnt + "].deleted\" value=\"true\" />");
									}
									sourceCnt++;
								});
								
								
								$('#frm_transfer').attr('action', '<c:url value="/transfer/request/list" />' + '<c:out value="/${repositoryKey}/save"/>');
								$('#frm_transfer').submit();
							};
						</script>
					</c:if>
					<c:if test="${transferStateAuth.isApproveCancelable}">
						<a class="button red mt ml" onclick="approveCancelTransfer()"><small class="icon minus"></small><span>Cancel Approve</span></a>
						<script type="text/javascript" >
							function approveCancelTransfer(){
								$('#frm_transfer').attr('action', '<c:url value="/transfer/request/list" />' + '<c:out value="/${repositoryKey}/approveCancel"/>');
								$('#frm_transfer').submit();
							};
						</script>
					</c:if>
					<a class="button yellow mt ml" onclick="history.back()"><small class="icon play"></small><span>Back to List</span></a>
				</p>
			</form:form>
		</div>
	</div>
	<div class="clear"></div>
</div>



<div id="div_transferSourceDetail" class="transferSourceDetailPanel display-none">
	<span onclick="toggleTransferSourceDetail(this)">
		<a class="pmOpener closed">
			<img class="pClosed" src="<c:url value="/images/plus_small_white.png"/>"/><img class="mOpened" src="<c:url value="/images/minus_small_white.png"/>"/>
		</a>
		<span class="src-header">
			<span class="type"></span>
			<font class="path font12 revision open-window">
				<a></a>
			</font>
		</span>
		<span class="src-tail">
			<font class="path font12 open-window">
				<a></a>
			</font>
		</span>
	</span>
	
	<div class="box display-none transferSourceDetail">
		<div class="head"><div></div></div>
		<div class="desc">
			<div class="division change-info">
				<div class="sample display-none">
					<p>
						<span>commited  by <b class="author"></b>, at <b class="date"></b></span>
					</p>
					<pre class="message">
					</pre>
				</div>
			</div>
		
			<div>
				<c:if test="${transferStateAuth.isEditable}">
					<input class="chg-revision" type="button" value="Change Revision"/>
				</c:if>
				<input class="diff-prod" type="button" value="Diff with Prod"/>
				<c:if test="${transferStateAuth.isEditable}">
					<input class="remove" type="button" value="Remove"/>
				</c:if>
			</div>	
						
		</div>
		<div class="bottom"><div></div></div>
	</div>
	
</div>

<div id="div_searchSourceByName" title="Search Source" style="display:none;">
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
	
	  		<div id="div_sourceTreePanel" class="sourceTreePanel ui-widget-content" >
	  			<div id="div_sourceTree"></div>
	  		</div>
	  		
	  		<div class="sourceListPanel">
	  			<table id="tbl_sourceListByName" class="compact">
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

<div id="div_searchSourceByRevision" title="Search Source" style="display:none;">
	<div class="module text">
		<div>
	
	  		<div id="div_sourceChangePanel" class="sourceTreePanel" >
	  			
	  			<table id="tbl_changeListByRevision" class="compact">
	  				<thead>
	  					<tr>
	  						<th>Revision</th>
	  						<th>Author</th>
	  						<th>Date</th>
	  					</tr>
	  				</thead>
					<tbody>
						<tr class="sample">
							<td class="revision"></td>
							<td class="author"></td>
							<td class="date"></td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="3" style="text-align:center;">
								<span class="showmore display-none"><font class="path"><a onclick="retrieveRepositoryChangeListByRevision('<c:out value="${repository.trunkPath}" />')">Show More</a></font></span>
								<span class="loader display-none"><img src="<c:url value="/images/ajax-loader.gif"/>" /></span>
								<span class="nodata">no data</span>
							</td>
						</tr>
					</tfoot>
				</table>
				
				
	  		</div>
	  		
	  		<div class="sourceListPanel">
	  			<table id="tbl_sourceListByRevision" class="compact">
	  				<thead>
	  					<tr>
	  						<th class="w_130">Action</th>
	  						<th>File path</th>
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

<div id="div_searchRevision" title="Search Revision" style="display:none;">
	<div class="module text">
		<div class="changeListPanel">
			<table id="tbl_changeList" class="compact">
				<thead>
					<tr>
						<th class="w_100">Action</th>
						<th class="w_60">Revision</th>
						<th class="w_170">Commit Log</th>
						<th class="w_70">Date</th>
						<th class="w_80">Author</th>
					</tr>
				</thead>
				<tbody>
					<tr class="sample">
						<td>
							<div>
							  	<div>
							    	<button class="action">Select Rev</button>
							    	<button>Select an action</button>
							  	</div>
							  	<ul>
							    	<li class="browse"><a>View Source</a></li>
							  	</ul>
							</div>
						</td>
						<td class="revision"></td>
						<td class="message"></td>
						<td class="date"></td>
						<td class="author"></td>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="5" style="text-align:center;">
							<span class="showmore display-none"><font class="path"><a onclick="retrieveRepositoryChangeList()">Show More</a></font></span>
							<span class="loader display-none"><img src="<c:url value="/images/ajax-loader.gif"/>" /></span>
							<span class="nodata">no data</span>
						</td>
					</tr>
				</tfoot>
			</table>
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

<div id="div_changeOccurMessage" style="display:none;">
	<p>
    	<span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 50px 0;"></span>
    	<span>There are some changes.</span>
  	</p>
  	<p>
  		If request without saving, changes will be lost. Proceed?
  	</p>
</div>

<div id="div_duplicateMessage" style="display:none;">
	<p>
    	<span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 50px 0;"></span>
    	<span><b style="break" class="path"></b> is already in the list.</span>
  	</p>
</div>

<div id="div_approveSuccessMessage" style="display:none;">
	<p>
    	<span class="ui-icon ui-icon-circle-check" style="float: left; margin: 0 7px 50px 0;"></span>
    	<span>Approved request successfully.</span>
  	</p>
  	<p>
  		Do you want to add this request to request group?
  	</p>
</div>

<div id="div_approveEmergencyMessage" style="display:none;">
	<p>
    	<span class="ui-icon ui-icon-circle-check" style="float: left; margin: 0 7px 50px 0;"></span>
    	<span>Emergency request will be transfered to production branch without [Stand by] step in Request group.</span>
  	</p>
  	<p>
  		<b>Do you want to transfer this source list to production now?</b>
  	</p>
</div>