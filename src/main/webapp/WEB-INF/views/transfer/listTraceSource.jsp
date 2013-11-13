<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		$("#sel_repository option[value='<c:out value="${repositoryKey}" />']").attr('selected', 'selected');
		//if( '<c:out value="${repositoryKey}" />'.length > 0 ) retrieveTransferList();
		
		$("#sel_repository").change(changeRepository);
		enableSearchSourceAutocomplete();
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
	
	
	
</script>

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
							<a class="button right form_submit yellow"><small class="icon looking_glass"></small><span>Search</span></a>
						</p>
					</form>
				</div>
				<div class="bottom"><div></div></div>
			</div>
			
			<table id="tbl_traceSourceList">
				<thead>
					<tr>
						<th>State</th>
						<th>Path</th>
						<th>Transferred Revision</th>
						<th>Requestor</th>
						<th>Description</th>
						<th>Request Date</th>
						<th>Approve Date</th>
						<th>Transfer Date</th>
					</tr>
				</thead>
				<tbody>
					<tr class="sample">
						<td class="transferSeq w_80"><font class="path open-window"><a></a></font></td>
						<td class="transferType w_80"></td>
						<td class="transferState w_70"></td>
						<td class="requestor w_90"></td>
						<td class="description"></td>
						<td class="requestDate w_90" style="text-align:center;"></td>
						<td class="approveDate w_90" style="text-align:center;"></td>
						<td class="transferDate w_90" style="text-align:center;"></td>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="8" style="text-align:center;">
							<span class="showmore display-none"><font class="path"><a onclick="retrieveTransferList()">Show More</a></font></span>
							<span class="loader display-none"><img src="<c:url value="/images/ajax-loader.gif"/>"/></span>
							<span class="nodata">no data</span>
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
	<c:set var="createTransferPathLink" value="${pageContext.request.contextPath}/transfer/request/list/${repositoryKey}/add"/>
	<a href="<c:out value="${createTransferPathLink}"/>" class="button green right"><small class="icon plus"></small><span>Create</span></a>
	<div class="clear"></div>
</div>