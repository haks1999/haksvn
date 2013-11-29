<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script src="<c:url value="/js/jquery.jsPlumb-1.5.4.js"/>" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
	$(function() {
		$("#sel_repository option[value='<c:out value="${repositoryKey}" />']").attr('selected', 'selected');
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
	
	
	
	function retrieveTraceSourceList(){
		$("#tbl_transferList tfoot span:not(.loading)").removeClass('display-none').addClass('display-none');
		$("#tbl_transferList tfoot span.loader").removeClass('display-none');
		$.get( "<c:url value="/transfer/traceSource/list/trace"/>",
				{repositoryKey: '<c:out value="${repositoryKey}" />',
				path: $('#txt_searchSource').val(),
				limit:100},
				function(data) {
					var trunkElemList = data.trunkElemList;
					var branchElemList = data.branchElemList;
					var tagElemList = data.tagElemList;
					var maxSize = (function(){
						var elemArr = [trunkElemList.length,branchElemList.length,tagElemList.length];
						elemArr.sort();
						return elemArr[elemArr.length-1];
					})();
					for( var inx = 0 ; inx < maxSize ; inx++ ){
						var row = $("#tbl_traceSourceList > tbody > .sample").clone();
						if(trunkElemList[inx]) $(row).find(".trunk").text(trunkElemList[inx].revision).attr("id",trunkElemList[inx].id).removeClass("display-none");
						if(branchElemList[inx]) $(row).find(".branch").text(branchElemList[inx].revision).attr("id",branchElemList[inx].id).removeClass("display-none");
						if(tagElemList[inx]) $(row).find(".tag").text(tagElemList[inx].name).attr("id",tagElemList[inx].id).removeClass("display-none");
						$(row).removeClass("sample");
						$('#tbl_traceSourceList > tbody').append(row);
					}
					
					
					var outBoundConnector = {				
							paintStyle:{lineWidth:2,strokeStyle:"#056"},
							hoverPaintStyle:{strokeStyle:"#D16954"},
							endpoint:"Blank",
							anchor:"Continuous"
							/*,
							overlays:[
							          ["Label", {													   					
								cssClass:"l1 component label",
								location:0.7,
								events:{
									"click":function(label, evt) {
										alert("clicked on label for connection " + label.component.id);
									}
								}
							}], 
							["PlainArrow", {location:1, width:10, length:8} ]]
							*/
						};
					var anchor = {
						lmrm : ["RightMiddle", "LeftMiddle"],
						rmlm : ["LeftMiddle", "RightMiddle"]
					};
					var outBoundConnectList = data.outBoundConnectList;
					for( var inx=  0 ; inx < outBoundConnectList.length ; inx++ ){
						var anchorType = (outBoundConnectList[inx].srcId.indexOf("tag") < 0)? anchor.lmrm:anchor.rmlm; 
						var labelSeq = (outBoundConnectList[inx].srcId.indexOf("trunk") > -1)? outBoundConnectList[inx].transferSeq:outBoundConnectList[inx].taggingSeq;
						var labelText = (outBoundConnectList[inx].srcId.indexOf("trunk") > -1)? ("req-"+labelSeq):("tag-"+labelSeq);
						var labelCssClass = "connLabel" + (labelText.length > 6?" long":"");
						var conn = jsPlumb.connect({
							source:outBoundConnectList[inx].srcId,
							target:outBoundConnectList[inx].destId,
							anchors:anchorType,
							overlays:[
								["Label", {													   					
									cssClass:labelCssClass,
									location:0.8,
									label:labelText,
									events:{
									"click":function(label, evt) {
										//alert("clicked on label for connection " + label.component.id);
										}
									}
								}], 
								["PlainArrow", {location:1, width:10, length:8} ]]
						}, outBoundConnector);
						conn.traceId = labelSeq;
						//conn.seq = 
					}
					outBoundConnector.connector = "StateMachine";
					var branchInBoundConnector = outBoundConnector;
					var trunkInBoundConnector = {				
							connector:"Flowchart",
							paintStyle:{lineWidth:3,strokeStyle:"#D4D4C7",dashstyle:"2 2",},
							hoverPaintStyle:{strokeStyle:"#dbe300"},
							endpoint:"Blank",
							anchor:"Continuous"
						};
					var inBoundConnectList = data.inBoundConnectList;
					for( var inx=  0 ; inx < inBoundConnectList.length ; inx++ ){
						var inboundConnector = inBoundConnectList[inx].srcId.indexOf('trunk') < 0 ? branchInBoundConnector:trunkInBoundConnector;
						jsPlumb.connect({
							source:inBoundConnectList[inx].srcId,
							target:inBoundConnectList[inx].destId,
							anchors:["BottomCenter", "TopCenter"]
						}, inboundConnector);
					}
					
					jsPlumb.bind("click", function(connection, originalEvent){
						//retrieveTransferSourceList(1);
						//var traceTooltip = $('#qtip-' + connection.traceId);
						//if( traceTooltip.length < 1 ){
						var	traceTooltip = $(connection).qtip({
								content: {
									text: function(event, api) {
							            $.ajax({ url: "<c:url value="/transfer/request/list/${repositoryKey}/1"/>" + "?json=1" })
							                .done(function(json) {
							                    api.set('content.text', "<b>bold</b>: not bold");
							                })
							                .fail(function(xhr, status, error) {
							                    api.set('content.text', status + ': ' + error);
							                });

							            return 'Loading...';
							        },
							        title: 'I have a button to my right!',
							        button: 'Close'
								},
						        position: {
						        	my: 'bottom left',
									at: 'top center',
						            target: 'mouse',
						            //viewport: $('#tbl_traceSourceList'),
						            //viewport: $(window),
						            adjust: {
										mouse: false,
										scroll: false
									}
						        },
						        show: false,
						        hide: false
							}).qtip('api');
						//}
						
						
					
						traceTooltip.reposition(originalEvent).show(originalEvent);
						/*
						traceTooltip.set({
							//'content.title': connection.testId,
							//'content.text': connection.testId
							content:{
								title : connection.testId,
								text : connection.testId
							}
						})
						.reposition(originalEvent).show(originalEvent);
						*/
					});
		});
	};
	
	
	function retrieveTransferSourceList(transferSeq){
		$.getJSON(
				"<c:url value="/transfer/request/list/${repositoryKey}/"/>" + transferSeq,
				{json:1},
	            function(result){
					alert( result );
				});
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
}
td div.trunk{
background-image:url(/haksvn/images/rounded_rec_blue_trace.png);
}
td div.branch{
background-image:url(/haksvn/images/rounded_rec_blue_trace.png);
}
td div.tag{
width: 130px;
background-image:url(/haksvn/images/rounded_rec_green_trace.png);
}
.connLabel{
text-align:center;
vertical-align:middle;
background-repeat:no-repeat;
background-position:center center;
width: 42px;
height:20px;
font-style: italic;
font-size:8pt;
cursor:pointer;
background-image:url(/haksvn/images/rounded_rec_white_conn.png);
}

.connLabel.long{
line-height:8px;
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
							<label for="path" class="w_50">Path</label> 
							<input id="txt_searchSource" class="text w_600" type="text" name="path"/>
							<a id="btn_searchTrace" class="button right yellow"><small class="icon looking_glass"></small><span>Trace</span></a>
						</p>
					</form>
				</div>
				<div class="bottom"><div></div></div>
			</div>
			
			<table id="tbl_traceSourceList">
				<thead>
					<tr>
						<th class="w_80">trunk</th>
						<th class="w_80">branch</th>
						<th class="w_70">tag</th>
					</tr>
				</thead>
				<tbody>
					<tr class="sample">
						<td class="w_80"><div class="elem trunk display-none"></div></td>
						<td class="w_80"><div class="elem branch display-none"></div></td>
						<td class="w_70"><div class="elem tag display-none"></div></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<div class="clear"></div>
</div>