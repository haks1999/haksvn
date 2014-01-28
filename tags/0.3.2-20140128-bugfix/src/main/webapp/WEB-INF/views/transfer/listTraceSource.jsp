<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script src="<c:url value="/js/jquery.jsPlumb-1.5.4.js"/>" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
	$(function() {
		$("#sel_repository option[value='<c:out value="${repository.repositoryKey}" />']").attr('selected', 'selected');
		//if( '<c:out value="${repositoryKey}" />'.length > 0 ) retrieveTransferList();
		
		$("#sel_repository").change(changeRepository);
		enableSearchSourceAutocomplete();
		$("#btn_searchTrace").click(function(){retrieveTraceSourceList();});
	});
	
	function changeRepository(){
		$("#frm_traceSource").attr('action','<c:url value="/transfer/traceSource/list"/>' + '/' + $("#sel_repository option:selected").val());
		$("#frm_traceSource").submit();
	};
	
	function enableSearchSourceAutocomplete(){
		$("#txt_searchSource").keypress(function(event) {
		    if (event.which == 13) {
		        event.preventDefault();
		        //searchSource();
		    }
		});
		
		$( "#txt_searchSource" ).autocomplete({
			source: function( request, response ) {
		          		$.postJSON( "<c:url value="/transfer/traceSource/list/search/path"/>",
		          					{	repositoryKey:'<c:out value="${repository.repositoryKey}"/>',
		          						path:$( "#txt_searchSource" ).val()
		          					}, 
		          					function(data){
		          						var resData = [];
			            				if (!data.length || data.length < 1) {
			            					resData = [{ noresult: true, noresultmsg: "No Result!"}];
			            				}else{
			            					for( var inx = 0 ; inx < data.length ; inx++ ){
				            					resData.push({path:data[inx]});
				            				}			            					
			            				}
			            				response(resData);
		        	  	});
		          
		        	},
			select: function( event, ui ) {
						if(ui.item.noresultmsg) return false;
				        this.value = ui.item.path;
				        return false;
					},
			minLength: 3,
			focus: 	function( event, ui ) {
						// prevent value inserted on focus
						return false;
		      		}
		})
		.data( "autocomplete" )._renderItem = function( ul, item ) {
			if( item.noresult ){
				return $( "<li>" )
		        .append( "<a class=\"italic\">" + item.noresultmsg + "</a>" )
		        .appendTo( ul ); 
			}else{
				return $( "<li>" )
		        .append( "<a>" + item.path + "</a>" )
		        .appendTo( ul );
			}
		     
		};
	};
	
	
	var _gSearchPath = '';
	function retrieveTraceSourceList(){
		$("#tbl_traceSourceList tfoot tr").addClass("display-none");
		haksvn.block.on();
		jsPlumb.reset();
		_gSearchPath = $('#txt_searchSource').val();
		$("#tbl_traceSourceList tbody tr:not(.sample)").remove();
		resetDisplayAndIncasePaths();
		$.get( "<c:url value="/transfer/traceSource/list/trace"/>",
				{repositoryKey: '<c:out value="${repository.repositoryKey}" />',
				path: $('#txt_searchSource').val(),
				limit:50},
				function(data) {
					displayAndIncasePaths(data.trunkPathList, data.branchPathList );
					resetDiffWithSelections({trunkPathList: data.trunkPathList, 
												branchPathList: data.branchPathList,
												trunkElemList: data.trunkElemList,
												branchElemList: data.branchElemList});
					var trunkElemList = data.trunkElemList;
					var branchElemList = data.branchElemList;
					var tagElemList = data.tagElemList;
					var maxSize = (function(){
						var elemArr = [trunkElemList.length,branchElemList.length,tagElemList.length];
						elemArr.sort(function(a,b){return a-b;});
						return elemArr[elemArr.length-1];
					})();
					
					if( maxSize < 1 ){
						$("#tbl_traceSourceList tfoot tr").removeClass("display-none");
						haksvn.block.off();
						return;
					}
					for( var inx = 0 ; inx < maxSize ; inx++ ){
						var row = $("#tbl_traceSourceList > tbody > .sample").clone();
						if(trunkElemList[inx]) $(row).find(".trunk").text("r" + trunkElemList[inx].revision).attr("id",trunkElemList[inx].id).removeClass("display-none").attr("revision",trunkElemList[inx].revision).addClass(trunkElemList[inx].isLatest?"latest":"");
						if(branchElemList[inx]) $(row).find(".branch").text("r" + branchElemList[inx].revision).attr("id",branchElemList[inx].id).removeClass("display-none").attr("revision",branchElemList[inx].revision).addClass(branchElemList[inx].isLatest?"latest":"");
						if(tagElemList[inx]) $(row).find(".tag").text(tagElemList[inx].tagName).attr("id",tagElemList[inx].id).removeClass("display-none").attr("revision",tagElemList[inx].revision).attr("isTag",true).addClass(tagElemList[inx].isLatest?"latest":"");
						$(row).removeClass("sample");
						$('#tbl_traceSourceList > tbody').append(row);
					}
					haksvn.block.off();
					
					var outBoundConnector = {				
							hoverPaintStyle:{strokeStyle:"#D16954"},
							endpoint:"Blank",
							anchor:"Straight"
						};
					var anchor = {
						lmrm : ["RightMiddle", "LeftMiddle"],
						rmlm : ["LeftMiddle", "RightMiddle"]
					};
					var outBoundConnectList = data.outBoundConnectList;
					for( var inx = 0 ; inx < outBoundConnectList.length ; inx++ ){
						if(!outBoundConnectList[inx].isInRange) continue;
						var anchorType = (outBoundConnectList[inx].srcId.indexOf("tag") < 0)? anchor.lmrm:anchor.rmlm; 
						var isTransferRequest = (outBoundConnectList[inx].srcId.indexOf("trunk") > -1);
						var labelSeq = (isTransferRequest)? outBoundConnectList[inx].transferSeq:outBoundConnectList[inx].taggingSeq;
						var labelText = (isTransferRequest)? ("req-"+labelSeq):("tag-"+labelSeq);
						var labelCssClass = "connLabel" + (labelText.length > 6?" long":"") + (outBoundConnectList[inx].isLatest?" latest":"");
						var conn = jsPlumb.connect({
							source:outBoundConnectList[inx].srcId,
							target:outBoundConnectList[inx].destId,
							anchors:anchorType,
							//connector:"Straight", 
							paintStyle:{lineWidth:2,strokeStyle:outBoundConnectList[inx].isLatest?"#2D5770":"#D4D4C7"},
							overlays:[
								["Label", {													   					
									cssClass:labelCssClass,
									location:0.8,
									label:labelText
								}], 
								["PlainArrow", {location:1, width:8, length:8} ]]
						}, outBoundConnector);
						conn.traceId = labelSeq;
						conn.transferGroupSeq = outBoundConnectList[inx].transferGroupSeq;
						conn.isTransferRequest = isTransferRequest;
					}
					outBoundConnector.connector = "StateMachine";
					var flowInBoundConnector = outBoundConnector;
					var notFlowInBoundConnector = {				
							connector:"Flowchart",
							hoverPaintStyle:{strokeStyle:"#dbe300",dashstyle:"2 2"},
							endpoint:"Blank",
							anchor:"Continuous"
						};
					var inBoundConnectList = data.inBoundConnectList;
					for( var inx=  0 ; inx < inBoundConnectList.length ; inx++ ){
						if(inBoundConnectList[inx].isFlow) disableDiffWithSelectionOptions(inBoundConnectList[inx].destId);
						var inboundConnector = inBoundConnectList[inx].isFlow ? flowInBoundConnector:notFlowInBoundConnector;
						var inboundPaintStyle = function(){
							if( inBoundConnectList[inx].isLatest ) return {lineWidth:2,strokeStyle:"#2D5770"};
							if( inBoundConnectList[inx].isFlow ) return {lineWidth:2,strokeStyle:"#B5B5B5"};
							return {lineWidth:2,strokeStyle:"#D4D4C7",dashstyle:"2 2"};
						}();
						jsPlumb.connect({
							source:inBoundConnectList[inx].srcId,
							target:inBoundConnectList[inx].destId,
							anchors:["BottomCenter", "TopCenter"],
							paintStyle:inboundPaintStyle
						}, inboundConnector);
					}
					
					jsPlumb.bind("click", retrieveAndViewConnectionDetail);
					$("#tbl_traceSourceList tbody td .trunk").click(retrieveAndViewElemDetail);
					$("#tbl_traceSourceList tbody td .branch").click(retrieveAndViewElemDetail);
					$("#tbl_traceSourceList tbody td .tag").click(retrieveAndViewElemDetail);
		});
	};
	
	var gTrunkPathSet = [],gBranchPathSet=[];
	function resetDisplayAndIncasePaths(){
		$("#div_trunkPathList .desc div").remove();	
		gTrunkPathSet = [];
		gBranchPathSet = [];
	};
	
	function displayAndIncasePaths(trunkPathList, branchPathList){
		var isLimit = false;
		for( var inx = 0 ; inx < trunkPathList.length ; inx++ ){
			if( inx > 9){
				isLimit = true;
				$("#div_trunkPathList .desc").append("<p>And " + (trunkPathList.length - 9) + " more sources</p>");
			}
			gTrunkPathSet[trunkPathList[inx]] = true;
			if( isLimit ) continue;
			var trunkPath = $("#div_trunkPathList .sample").clone();
			$(trunkPath).find("font a").text(trunkPathList[inx]);
			$(trunkPath).find("a").attr("href",'<c:url value="/source/browse/${repositoryKey}"/>' + trunkPathList[inx] + "?rev=-1");
			$(trunkPath).removeClass("sample").removeClass("display-none");
			$("#div_trunkPathList .desc").append(trunkPath);
		}
		for( var inx = 0 ; inx < branchPathList.length ; inx++ ){
			gBranchPathSet[branchPathList[inx]] = true;
		}
	};
	
	function fullPathToAbbrPath(path){
		var limit = 6;
		var pathArr = path.split("/");
		if( pathArr.length < limit ) return path; 
		return ".../" + pathArr.slice(limit, pathArr.length).join("/");
	}
	
	function resetDiffWithSelections( args ){
		var trunkPathList = args.trunkPathList;
		var branchPathList = args.branchPathList;
		var trunkElemList = args.trunkElemList;
		var branchElemList = args.branchElemList;
		$("#sel_diffWithPath option").remove();
		for( var inx = 0 ; inx < trunkPathList.length ; inx++ ){
			$("#sel_diffWithPath").append("<option value=\"" + trunkPathList[inx] + "\" class=\"trunk\">" + fullPathToAbbrPath(trunkPathList[inx]) + "</option>");
		}
		for( var inx = 0 ; inx < branchPathList.length ; inx++ ){
			$("#sel_diffWithPath").append("<option value=\"" + branchPathList[inx] + "\" class=\"branch display-none\">..." + fullPathToAbbrPath(branchPathList[inx]) + "</option>");
		}
		$("#sel_diffWithRevision option").remove();
		for( var inx = 0 ; inx < trunkElemList.length ; inx++ ){
			$("#sel_diffWithRevision").append("<option value=\"" + trunkElemList[inx].revision + "\" class=\"trunk\">r" + trunkElemList[inx].revision + "</option>");
		}
		for( var inx = 0 ; inx < branchElemList.length ; inx++ ){
			$("#sel_diffWithRevision").append("<option value=\"" + branchElemList[inx].revision + "\" class=\"branch display-none\">r" + branchElemList[inx].revision + "</option>");
		}
		$("#sel_diffWithRoot").change(function(){
			$("#sel_diffWithPath option." + $(this).val()).toggleOption(true);
			$("#sel_diffWithPath option:not(." + $(this).val() + ")").toggleOption(false);
			$("#sel_diffWithRevision option." + $(this).val()).toggleOption(true);
			$("#sel_diffWithRevision option:not(." + $(this).val() + ")").toggleOption(false);
		});
		$("#sel_diffWithRoot").val("trunk").change();
	};
	
	function disableDiffWithSelectionOptions(revision){
		$("#sel_diffWithRevision option[value=" + revision.replace("branch","") + "]").attr("disabled","disabled");
	};
	
	function retrieveAndViewElemDetail(event){
		var tooltipData = generateTooltipDataForRevisionElem($(this).attr("revision"));
		var isTagElem = $(this).attr("isTag");
		var	traceTooltip = $(this).qtip({
			content: {
				text: function(event, api) {
		            $.ajax({ url: tooltipData.url })
		                .done(function(json) {
		                    api.set('content.text', isTagElem?generateTooltipContentForTagRevision(json):generateTooltipContentForRevision(json));
		                })
		                .fail(function(xhr, status, error) {
		                    api.set('content.text', status + ': ' + error);
		                });
			            return 'Loading...';
		        },
		        title: tooltipData.title,
		        button: 'Close'
			},
	        position: {
	        	my: 'bottom left',
				at: 'top center',
	            target: 'mouse',
	            adjust: {
					mouse: false,
					scroll: false
				}
	        },
	        show: false,
	        hide: false,
	        style: {
	            classes: 'qtip-light qtip-shadow qtip-shadow qtip-rounded'
	        }
		}).qtip('api');

		traceTooltip.reposition(event).show(event);
	};
	
	
	function retrieveAndViewConnectionDetail(connection, originalEvent){
		if( !connection.traceId ) return;
		var tooltipData = connection.isTransferRequest?generateTooltipDataForTransfer(connection):generateTooltipDataForTagging(connection);
		var	traceTooltip = $(connection).qtip({
				content: {
					text: function(event, api) {
			            $.ajax({ url: tooltipData.url })
			                .done(function(json) {
			                    api.set('content.text', connection.isTransferRequest?generateTooltipContentForTransfer(json):generateTooltipContentForTagging(json));
			                })
			                .fail(function(xhr, status, error) {
			                    api.set('content.text', status + ': ' + error);
			                });

			            return 'Loading...';
			        },
			        title: tooltipData.title,
			        button: 'Close'
				},
		        position: {
		        	my: 'bottom left',
					at: 'top center',
		            target: 'mouse',
		            adjust: {
						mouse: false,
						scroll: false
					}
		        },
		        show: false,
		        hide: false,
		        style: {
		            classes: 'qtip-shadow qtip-shadow qtip-rounded'
		        }
			}).qtip('api');
	
		traceTooltip.reposition(originalEvent).show(originalEvent);
	};
	
	function generateTooltipDataForRevisionElem(revision){
		var titleElem = $("#div_tipTitle").clone();
		titleElem.removeAttr("id").find("b").text("Revision Info: ");
		titleElem.find("font a").text( "r" + revision);
		titleElem.find("a").attr("href",'<c:url value="/source/changes/${repositoryKey}"/>' + "?rev=" + revision);
		var title = titleElem.html();
		return {url: "<c:url value="/source/changes/search"/>" + "?repositoryKey=<c:out value="${repository.repositoryKey}"/>" + "&path=" + escape("") + "&rev=" + revision,
				title:title};
	};
	
	function generateTooltipDataForTransfer(connection){
		var transferSeq = connection.traceId;
		var url = "<c:url value="/transfer/request/list/${repository.repositoryKey}/"/>" + transferSeq + "?json=1";
		var titleElem = $("#div_tipTransferTitle").clone();
		titleElem.find(".req font a").text( "req-" + transferSeq);
		titleElem.find(".req a").attr("href","<c:url value="/transfer/request/list/${repository.repositoryKey}/"/>" + transferSeq);
		titleElem.find(".group font a").text( "group-" + connection.transferGroupSeq);
		titleElem.find(".group a").attr("href","<c:url value="/transfer/requestGroup/list/${repository.repositoryKey}/"/>" + connection.transferGroupSeq);
		var title = titleElem.html();
		return {url:url, title:title};
	};
	
	function generateTooltipContentForTransfer(result){
		var contentElem = $("#div_tipTransferContent").clone();
		contentElem.removeAttr("id").find("span.type").text(result.transferTypeCode.codeName);
		contentElem.find("span.state").text(result.transferStateCode.codeName);
		contentElem.find("span.requested").text(result.requestUser.userName + "(" + result.requestUser.userId + "), " + haksvn.date.convertToEasyFormat(new Date(result.requestDate)));
		contentElem.find("span.approved").text(result.approveUser.userName + "(" + result.approveUser.userId + "), " + haksvn.date.convertToEasyFormat(new Date(result.approveDate)));
		contentElem.find("span.description").text(result.description);
		for( var inx = 0 ; inx < result.sourceList.length;inx++ ){
			var fullPath = result.sourceList[inx].path;
			var sourcePathListElem = $("#div_sourcePathList li").clone();
			var isIncasePath = gTrunkPathSet[fullPath];
			if( !isIncasePath){
				sourcePathListElem.find("img.diff").remove();
			}else{
				sourcePathListElem.find("img.diff").attr("onclick","openDiffWithDialog(\""+ result.sourceList[inx].path + "\",\""+result.sourceList[inx].revision +"\");");
			}
			sourcePathListElem.find("font a").text( fullPath.substr(fullPath.lastIndexOf("/")+1));
			sourcePathListElem.find("a").attr("href",'<c:url value="/source/browse/${repositoryKey}"/>' + fullPath + "?rev=" + result.sourceList[inx].revision)
				.addClass(gTrunkPathSet[fullPath]?"incase":"");
			contentElem.find("ul").append(sourcePathListElem);
		}
		return $(contentElem).wrap('<div>').parent().html();
	};
	
	function generateTooltipDataForTagging(connection){
		var taggingSeq = connection.traceId;
		var url = "<c:url value="/transfer/tagging/list/${repository.repositoryKey}/"/>" + taggingSeq + "?json=1";
		var titleElem = $("#div_tipTitle").clone();
		titleElem.removeAttr("id").find("b").text("Tagging Info: ");
		titleElem.find("font a").text( "tagging-" + taggingSeq);
		titleElem.find("a").attr("href","<c:url value="/transfer/tagging/list/${repository.repositoryKey}/"/>" + taggingSeq);
		var title = titleElem.html();
		return {url:url, title:title};
	};
	
	function generateTooltipContentForTagging(result){
		var contentElem = $("#div_tipTaggingContent").clone();
		contentElem.removeAttr("id").find("span.type").text(result.taggingTypeCode.codeName);
		contentElem.find("span.tagged").text(result.taggingUser.userName + "(" + result.taggingUser.userId + "), " + haksvn.date.convertToEasyFormat(new Date(result.taggingDate)));
		contentElem.find("span.description").text(result.description);
		return $(contentElem).wrap('<div>').parent().html();
	};
	
	function generateTooltipContentForRevision(result){
		var contentElem = $("#div_tipRevisionContent").clone();
		contentElem.removeAttr("id").find("span.author").text(result.log.author);
		contentElem.find("span.commited").text(result.log.author + ", " + haksvn.date.convertToEasyFormat(new Date(result.log.date)));
		contentElem.find("pre.message").text(result.log.message);
		
		var sourceList = result.log.changedList;
		for( var inx = 0 ; inx < sourceList.length;inx++ ){
			if( sourceList[inx].nodeType != 'file' ) continue;
			var fullPath = sourceList[inx].path;
			var sourcePathListElem = $("#div_sourcePathList li").clone();
			var isIncasePath = gTrunkPathSet[fullPath]||gBranchPathSet[fullPath];
			if( !isIncasePath){
				sourcePathListElem.find("img.diff").remove();
			}else{
				sourcePathListElem.find("img.diff").attr("onclick","openDiffWithDialog(\""+ fullPath + "\",\""+ result.revision +"\");");
			}
			sourcePathListElem.find("font a").text( fullPath.substr(fullPath.lastIndexOf("/")+1));
			sourcePathListElem.find("a").attr("href",'<c:url value="/source/browse/${repositoryKey}"/>' + fullPath + "?rev=" + result.revision)
				.addClass(isIncasePath?"incase":"");
			contentElem.find("ul").append(sourcePathListElem);
		}
		
		return $(contentElem).wrap('<div>').parent().html();
	};
	
	function generateTooltipContentForTagRevision(result){
		var contentElem = $("#div_tipRevisionContent").clone();
		contentElem.removeAttr("id").find("span.author").text(result.log.author);
		contentElem.find("span.commited").text(result.log.author + ", " + haksvn.date.convertToEasyFormat(new Date(result.log.date)));
		contentElem.find("pre.message").text(result.log.message);
		
		var sourceList = result.log.changedList;
		for( var inx = 0 ; inx < sourceList.length;inx++ ){
			var fullPath = sourceList[inx].path;
			var sourcePathListElem = $("#div_sourcePathList li").clone();
			sourcePathListElem.find("img.diff").remove();
			sourcePathListElem.find("font a").text( fullPath.substr(fullPath.lastIndexOf("/")+1));
			sourcePathListElem.find("a").attr("href",'<c:url value="/source/browse/${repositoryKey}"/>' + fullPath)
				.addClass("incase");
			contentElem.find("ul").append(sourcePathListElem);
		}
		
		return $(contentElem).wrap('<div>').parent().html();
	};
	
	function openDiffWithDialog( path, revision ){
		$("#pre_diffResult").html("<br/><br/>");
		$("#div_diffWith").data({path:path, revision:revision});
		$("#ipt_diffWith").click(function(){
			retrieveDiffWithRevisions({
				srcPath: $("#div_diffWith").data("path"),
				srcRev: $("#div_diffWith").data("revision"),
				trgPath: $("#sel_diffWithPath").val(),
				trgRev: $("#sel_diffWithRevision").val()
			});
		});
		$("#ipt_diffWithSideBySide").click(function(){
			openDiffWithSideBySidePage({
				srcPath: $("#div_diffWith").data("path"),
				srcRev: $("#div_diffWith").data("revision"),
				trgPath: $("#sel_diffWithPath").val(),
				trgRev: $("#sel_diffWithRevision").val()
			});
		});
		$("#div_diffWith span.path").find("font a").text( path );
		$("#div_diffWith span.path").find("a").attr("href",'<c:url value="/source/browse/${repositoryKey}"/>' + path + "?rev=" + revision);
		$("#div_diffWith span.revision").find("font a").text( "r" + revision );
		$("#div_diffWith span.revision").find("a").attr("href",'<c:url value="/source/changes/${repositoryKey}"/>' + "?rev=" + revision);
		$("#div_diffWith").dialog({
			resizable:false,
			height: 470,
		    width: 750,
		    title: "Diff with",
		    modal: true,
		    buttons: {
		    	"Close": function() {
		            $( this ).dialog( "close" );
		        }
		    }
	    });
	};
	
	function retrieveDiffWithRevisions(args){
		$("#pre_diffResult").html("<br/><br/>");
		$("#pre_diffResult").addClass('loading');
		$.getJSON( "<c:url value="/source/changes/diff"/>",
				{repositoryKey: '<c:out value="${repositoryKey}" />',
				srcPath: args.srcPath,
				srcRev: args.srcRev,
				trgPath: args.trgPath,
				trgRev: args.trgRev,
				json:true},
				function(data) {
					$("#pre_diffResult").removeClass('loading');
					$("#pre_diffResult").html(data.diffToHtml);
		});
	};
	
	function openDiffWithSideBySidePage(args){
		var param = '?repositoryKey=' + '<c:out value="${repository.repositoryKey}"/>'
					+ '&path=' + args.srcPath
					+ '&srcRev=' + args.srcRev
					+ '&srcPath=' + args.trgPath
					+ '&trgRev=' + args.trgRev;
		window.open('<c:url value="/source/changes/diff" />' + param );
	};
</script>

<style type="text/css">
path:hover{cursor:pointer;}
#tbl_traceSourceList tr:hover td {background:none;}
#tbl_traceSourceList td{border:none;width:330px;}
#tbl_traceSourceList td div.elem{
background-repeat:no-repeat;
background-position:center center;
text-align:center;
vertical-align:middle;
width: 100px;
margin:5px;
padding:8px;
cursor:pointer;
}
#tbl_traceSourceList td div.elem:hover{
color:#483D8B;
font-weight:bold;
text-decoration:underline;
}
#tbl_traceSourceList th div.elem{
vertical-align:middle;
margin:0 5px 0 30px;
padding:0 8px 0 8px;
}
#tbl_traceSourceList td div.trunk{
background-image:url("<c:url value="/images/rounded_rec_blue_trace.png" />");
}
#tbl_traceSourceList td div.trunk.latest{
color:#227387;
font-weight:bold;
background-image:url("<c:url value="/images/rounded_rec_blue_trace_border.png" />");
}
#tbl_traceSourceList td div.branch{
background-image:url("<c:url value="/images/rounded_rec_blue_trace.png" />");
}
#tbl_traceSourceList td div.branch.latest{
color:#227387;
font-weight:bold;
background-image:url("<c:url value="/images/rounded_rec_blue_trace_border.png" />");
}
#tbl_traceSourceList td div.tag{
width: 130px;
background-image:url("<c:url value="/images/rounded_rec_green_trace.png" />");
}
#tbl_traceSourceList td div.tag.latest{
color:#41805A;
font-weight:bold;
background-image:url("<c:url value="/images/rounded_rec_green_trace_border.png" />");
}
.connLabel{
text-align:center;
vertical-align:middle;
background-repeat:no-repeat;
background-position:center center;
width: 42px;
height:25px;
line-height:25px;
font-style: italic;
font-size:8pt;
cursor:pointer;
background-image:url("<c:url value="/images/rounded_rec_gray_conn.png" />");
}
.connLabel:hover{
color:black;
text-decoration:underline;
}
.connLabel.long{
padding-top:2px;
line-height:10px;
}
.connLabel.latest{
color:#D4D4C7;
background-image:url("<c:url value="/images/rounded_rec_indigo_conn.png" />");
}

.tooltipContent p,.tooltipContent pre{
font-size: 11px;
margin:0px 0px 2px 0px;
}
.tooltipContent div.sourcePathList{
height:100px;
overflow-y:scroll;
}
.tooltipContent hr{
border-top: 1px solid #ccc;
}
.tooltipContent ul{
margin:0px 0px 0px 5px;
list-style-type: none;
}
.tooltipContent li{
margin:0px;
}
.tooltipContent li a{
font-size: 11px;
color:gray;
}
.tooltipContent li a.incase{
font-size: 11px;
font-weight:bold;
color:#483d8b;
}
img.diff{
height:10px;
margin-left:2px; 
cursor:pointer;
}
.ui-dialog{
z-index:18000 !important ;
}
select option{
width:auto;
}
</style>
<div class="content-page">
	<div class="col w10 last">
		<div class="content">
			<div class="box">
				<div class="head"><div></div></div>
				<div class="desc search">
					<form id="frm_traceSource" method="get">
						<p>
							<label for="sel_repository" class="w_120">Repository Name</label> 
							<select id="sel_repository">
								<c:forEach items="${repositoryList}" var="repository">
									<option value="<c:out value="${repository.repositoryKey}"/>">
										<c:out value="${repository.repositoryName}" />
									</option>
								</c:forEach>
							</select>
							<label for="path" class="w_60">Path</label> 
							<input id="txt_searchSource" class="text w_500" type="text" name="path"/>
							<a id="btn_searchTrace" class="button right yellow"><small class="icon looking_glass"></small><span>Trace</span></a>
						</p>
					</form>
				</div>
				<div class="bottom"><div></div></div>
			</div>
			
			<div id="div_trunkPathList" class="info">
				<div class="tl"></div>
				<div class="tr"></div>
				<div class="desc"><p>Show only latest 50 traces by performance issue</p></div>
				<div class="display-none sample"><font class="path open-window"><a></a></font></div>
				<div class="bl"></div>
				<div class="br"></div>
			</div>
			
			<table id="tbl_traceSourceList" style="width:990px;">
				<thead>
					<tr>
						<th style="border-top:none;"><div class="elem">Trunk</div></th>
						<th style="border-top:none;"><div class="elem">Branch</div></th>
						<th style="border-top:none;"><div class="elem">Tag</div></th>
					</tr>
				</thead>
				<tbody>
					<tr class="sample">
						<td><div class="elem trunk display-none"></div></td>
						<td><div class="elem branch display-none"></div></td>
						<td><div class="elem tag display-none"></div></td>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="3" style="text-align:center;">
							<span class="nodata">no data</span>
						</td>
					</tr>
				</tfoot>
			</table>
			
		</div>
	</div>
	<div class="clear"></div>
</div>

<div class="display-none">
	<div id="div_tipTransferTitle">
		<b class="req"><font class="path open-window"><a href=""></a></font></b>&nbsp;&nbsp;in <b class="group"><font class="path open-window"><a href=""></a></font></b>
	</div>
	<div id="div_tipTitle">
		<b></b><font class="path open-window" style="font-weight:bold;"><a href=""></a></font>
	</div>
	<div id="div_tipTransferContent" class="tooltipContent">
		<p><b>Type: </b><span class="type"></span></p>
		<p><b>State: </b><span class="state"></span></p>
		<p><b>Requested: </b><span class="requested"></span></p>
		<p><b>Appproved: </b><span class="approved"></span></p>
		<p><b>description: </b><span class="description"></span></p>
		<hr/>
		<div class="sourcePathList"><ul></ul></div>
	</div>
	<div id="div_tipTaggingContent" class="tooltipContent">
		<p><b>Type: </b><span class="type"></span></p>
		<p><b>Tagged: </b><span class="tagged"></span></p>
		<p><b>description: </b><span class="description"></span></p>
	</div>
	<div id="div_tipRevisionContent" class="tooltipContent">
		<p><b>Commited: </b><span class="commited"></span></p>
		<p><b>Commit Message: </b></p>
		<pre class="message"></pre>
		<hr/>
		<div class="sourcePathList"><ul></ul></div>
	</div>
	<div id="div_sourcePathList" class="tooltipContent">
		<ul><li><font class="path open-window"><a href=""></a></font><img class="diff" src="<c:url value="/images/diff_with.png"/>" title="Diff with..." /></li></ul>
	</div>
	<div id="div_diffWith">
		<p>
			<b>Source: </b>
			<span class="path"><font class="path open-window"><a href=""></a></font></span>
			<span class="revision"><font class="path open-window"><a href=""></a></font></span>
		</p>
		<div class="box">
			<div class="head"><div></div></div>
			<div class="desc search">
				<p>
					<select id="sel_diffWithRoot">
						<option value="trunk">trunk</option>
						<option value="branch">branch</option>
					</select>
					<select id="sel_diffWithPath">
					</select>
					<select id="sel_diffWithRevision">
					</select>
					<input id="ipt_diffWith" type="button" value="diff" />
					<input id="ipt_diffWithSideBySide" type="button" value="Side by Side" />
				</p>
			</div>
			<div class="bottom"><div></div></div>
		</div>
		<div style="height:240px;border:1px solid #ccc;overflow-y:scroll;">
			<pre id="pre_diffResult" class="diff">
			</pre>
		</div>
	</div>
</div>


    