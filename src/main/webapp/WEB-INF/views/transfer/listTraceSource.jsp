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
		haksvn.block.on();
		jsPlumb.reset();
		_gSearchPath = $('#txt_searchSource').val();
		$("#tbl_traceSourceList tbody tr:not(.sample)").remove();
		$.get( "<c:url value="/transfer/traceSource/list/trace"/>",
				{repositoryKey: '<c:out value="${repository.repositoryKey}" />',
				path: $('#txt_searchSource').val(),
				limit:100},
				function(data) {
					var trunkElemList = data.trunkElemList;
					var branchElemList = data.branchElemList;
					var tagElemList = data.tagElemList;
					var maxSize = (function(){
						var elemArr = [trunkElemList.length,branchElemList.length,tagElemList.length];
						elemArr.sort(function(a,b){return a-b;});
						return elemArr[elemArr.length-1];
					})();
					for( var inx = 0 ; inx < maxSize ; inx++ ){
						var row = $("#tbl_traceSourceList > tbody > .sample").clone();
						if(trunkElemList[inx]) $(row).find(".trunk").text(trunkElemList[inx].revision).attr("id",trunkElemList[inx].id).removeClass("display-none").attr("revision",trunkElemList[inx].revision).addClass(trunkElemList[inx].isLatest?"latest":"");
						if(branchElemList[inx]) $(row).find(".branch").text(branchElemList[inx].revision).attr("id",branchElemList[inx].id).removeClass("display-none").attr("revision",branchElemList[inx].revision).addClass(branchElemList[inx].isLatest?"latest":"");
						if(tagElemList[inx]) $(row).find(".tag").text(tagElemList[inx].name).attr("id",tagElemList[inx].id).removeClass("display-none").attr("name",tagElemList[inx].name).addClass(tagElemList[inx].isLatest?"latest":"");
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
						conn.srcRevision = outBoundConnectList[inx].srcRevision;
						conn.destRevision = outBoundConnectList[inx].destRevision;
						conn.traceId = labelSeq;
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
		});
	};
	
	function retrieveAndViewElemDetail(event){
		var tooltipData = generateTooltipDataForRevisionElem($(this).attr("revision"));
		var	traceTooltip = $(this).qtip({
			content: {
				text: function(event, api) {
		            $.ajax({ url: tooltipData.url })
		                .done(function(json) {
		                    api.set('content.text', generateTooltipContentForRevision(json));
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
		return {url: "<c:url value="/source/changes/search"/>" + "?repositoryKey=<c:out value="${repository.repositoryKey}"/>" + "&path=" + escape("/") + "&rev=" + revision,
				title:title};
	};
	
	function generateTooltipDataForTransfer(connection){
		var transferSeq = connection.traceId;
		var url = "<c:url value="/transfer/request/list/${repository.repositoryKey}/"/>" + transferSeq + "?json=1";
		var titleElem = $("#div_tipTitle").clone();
		//titleElem.removeAttr("id").find("b").text($("#"+connection.sourceId).attr("revision") + " r" + $("#"+connection.targetId).attr("revision") + " ");
		titleElem.find("font a").text( "REQ-" + transferSeq);
		titleElem.find("a").attr("href","<c:url value="/transfer/request/list/${repository.repositoryKey}/"/>" + transferSeq);
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
		return $(contentElem).wrap('<div>').parent().html();
	};
</script>

<style type="text/css">
path:hover{cursor:pointer;}
table tr:hover td {background:none;}
table td{border:none;}
td div.elem{
background-repeat:no-repeat;
background-position:center center;
text-align:center;
vertical-align:middle;
width: 100px;
margin:5px;
padding:8px;
cursor:pointer;
}
td div.elem:hover{
color:#483D8B;
font-weight:bold;
text-decoration:underline;
}
th div.elem{
text-align:center;
vertical-align:middle;
width: 100px;
margin:0 5px 0 5px;
padding:0 8px 0 8px;
}
td div.trunk{
background-image:url(/haksvn/images/rounded_rec_blue_trace.png);
}
td div.trunk.latest{
color:#227387;
font-weight:bold;
background-image:url(/haksvn/images/rounded_rec_blue_trace_border.png);
}
td div.branch{
background-image:url(/haksvn/images/rounded_rec_blue_trace.png);
}
td div.branch.latest{
color:#227387;
font-weight:bold;
background-image:url(/haksvn/images/rounded_rec_blue_trace_border.png);
}
td div.tag{
width: 130px;
background-image:url(/haksvn/images/rounded_rec_green_trace.png);
}
td div.tag.latest{
color:#41805A;
font-weight:bold;
background-image:url(/haksvn/images/rounded_rec_green_trace_border.png);
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
background-image:url(/haksvn/images/rounded_rec_gray_conn.png);
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
background-image:url(/haksvn/images/rounded_rec_indigo_conn.png);
}

.tooltipContent p,.tooltipContent pre{
font-size: 11px;
margin:0px 0px 2px 0px;
}

form p label.options{
font-style:italic;font-size:12px;line-height:20px;
}
form p label.options input{
vertical-align: bottom;
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
							<input id="txt_searchSource" class="text w_600" type="text" name="path"/>
							<a id="btn_searchTrace" class="button right yellow"><small class="icon looking_glass"></small><span>Trace</span></a>
						</p>
						<p>
							<label class="w_120">Options</label> 
							<label for="trunkCommit" class="options">
								<input id="ckb_trunkCommit" type="checkbox" name="trunkCommit"/>
								Include Trunk Commits
							</label> 
							<label for="onlyComplete" class="options">
								<input id="ckb_onlyComplete" type="checkbox" name="onlyComplete" checked="checked"/>
								Only Completed Requets
							</label> 
						</p>
					</form>
				</div>
				<div class="bottom"><div></div></div>
			</div>
			
			<div id="div_syncTaggingInfo" class="info">
				<div class="tl"></div>
				<div class="tr"></div>
				<div class="desc variable-help">Latest Tag synchronized with production branch: <font class="path open-window"><a></a></font></div>
				<div class="bl"></div>
				<div class="br"></div>
			</div>
			
			<table id="tbl_traceSourceList">
				<thead>
					<tr>
						<th><div class="elem">trunk</div></th>
						<th><div class="elem">branch</div></th>
						<th><div class="elem">tags</div></th>
					</tr>
				</thead>
				<tbody>
					<tr class="sample">
						<td><div class="elem trunk display-none"></div></td>
						<td><div class="elem branch display-none"></div></td>
						<td><div class="elem tag display-none"></div></td>
					</tr>
				</tbody>
			</table>
			
		</div>
	</div>
	<div class="clear"></div>
</div>

<div class="display-none">
	<div id="div_tipTitle">
		<b></b><font class="path open-window" style="font-weight:bold;"><a href=""></a></font>
	</div>
	<div id="div_tipTransferContent" class="tooltipContent">
		<p><b>Type: </b><span class="type"></span></p>
		<p><b>State: </b><span class="state"></span></p>
		<p><b>Requested: </b><span class="requested"></span></p>
		<p><b>Appproved: </b><span class="approved"></span></p>
		<p><b>description: </b><span class="description"></span></p>
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
	</div>
</div>