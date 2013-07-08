<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<style>
form p span font a{text-decoration:underline;cursor:pointer;}
.requestDetail{margin-left:20px;margin-top:5px;width:auto;}
.requestDetail .requestSourceList{min-height:20px;}
.requestDetail .requestSourceList li{margin:0px;}
.requestDetail .requestSourceList li .type{display:inline-block;width:50px;}
.requestDetail .requestSourceList li .revision{display:inline-block;width:50px;}
</style>
<script type="text/javascript">
	
	$(function() {
		$('#frm_transferGroup').attr('action', '<c:url value="/transfer/requestGroup/list/${repositorySeq}/save" />');
		transformDateField();
		setFormValidation();
		if( Number('<c:out value="${transferGroup.transferGroupSeq}"/>') > 0) retrieveAddedRequestList();
   	});
	
	var validTransferGroupForm;
	function setFormValidation(){
		validTransferGroupForm = $("#frm_transferGroup").validate({
			rules: {
				title: {
					required: true,
					minlength: 10,
					maxlength: 50
				},
				description: {
					required: true,
					minlength: 10,
					maxlength: 2000
				},
				transferListCount: {
					required: true,
					minSelect: 1
				}
			},
			submitHandler : function(form){
				if( !validTransferGroupForm.valid() ) return false;
				haksvn.block.on();
				form.submit();
			}
		});
	};
	
	function transformDateField(){
		var transferDate = Number('<c:out value="${transferGroup.transferDate}"/>');
		if( transferDate > 0 ) $('#frm_transferGroup input.transferDate').val(haksvn.date.convertToComplexFullFormat(new Date(transferDate)));
	};
	
	
	function openSearchApprovedRequestDialog(){
		initApprovedRequestList();
		$("#div_searchApprovedRequest").dialog({
			resizable:false,
			height: 450,
		    width: 600,
		    modal: true,
		    buttons: {
		    	"Add" : function(){
		    		addRequestToTransferList();
		    		$( this ).dialog( "close" );
		    	},
		    	"Close": function() {
		            $( this ).dialog( "close" );
		        }
		    }
	    });
		retrieveApprovedRequestList();
	};
	
	function initApprovedRequestList(){
		_gApprovedRequestPaging.start=-1;
		$("#tbl_approvedRequestList tbody tr:not(.sample)").remove();
		$("#tbl_approvedRequestList tfoot").removeClass('display-none');
	};
	
	var _gApprovedRequestPaging = {start:-1,limit:50};
	function retrieveApprovedRequestList(){
		$("#tbl_approvedRequestList tfoot span:not(.loader)").removeClass('display-none').addClass('display-none');
		$("#tbl_approvedRequestList tfoot span.loader").removeClass('display-none');
		_gApprovedRequestPaging.sCode = "<spring:eval expression="T(com.haks.haksvn.common.code.util.CodeUtils).getTransferApprovedCodeId()"/>";
		$.post( "<c:url value="/transfer/request/list"/>" + "/" + '<c:out value="${repositorySeq}" />',
				_gApprovedRequestPaging,
				function(data) {
					var transferList = data.model;
					_gApprovedRequestPaging.start = data.start + transferList.length;
					for( var inx = 0 ; inx < transferList.length ; inx++ ){
						var row = $("#tbl_approvedRequestList > tbody > .sample").clone();
						//$(row).attr('transferSeq',transferList[inx].transferSeq);
						$(row).data('transfer',transferList[inx]);
						$(row).find(".transferSeq font a").text('req-'+transferList[inx].transferSeq);
						$(row).find(".transferSeq a").attr("href",'<c:url value="/transfer/request/list"/>' + '/' + transferList[inx].repositorySeq + '/' +  transferList[inx].transferSeq);
						$(row).children(".requestor").text(transferList[inx].requestUser.userName);
						$(row).children(".approver").text(transferList[inx].approveUser.userName);
						$(row).children(".description").text(transferList[inx].description);
						$(row).attr('transferSeq',transferList[inx].transferSeq).attr('repositorySeq',transferList[inx].repositorySeq);
						$(row).removeClass("sample");
						$('#tbl_approvedRequestList > tbody').append(row);
					}
					if( transferList.length < 1 ){
						$("#tbl_approvedRequestList tfoot span:not(.nodata)").removeClass('display-none').addClass('display-none');
						$("#tbl_approvedRequestList tfoot span.nodata").removeClass('display-none');
					}else if( transferList.length < _gApprovedRequestPaging.limit ){
						$("#tbl_approvedRequestList tfoot").removeClass('display-none').addClass('display-none');
					}else{
						$("#tbl_approvedRequestList tfoot span:not(.showmore)").removeClass('display-none').addClass('display-none');
						$("#tbl_approvedRequestList tfoot span.showmore").removeClass('display-none');
					};
		},'json');
	};
	
	var _gTransferGroupChanged = false;
	function retrieveAddedRequestList(){
		haksvn.block.on();
		$.getJSON(
				"<c:url value="/transfer/requestGroup/list/${repositorySeq}/${transferGroup.transferGroupSeq}/requests"/>",
				{},
	            function(result){
					for( var inx = 0 ; inx < result.length ; inx++){
						addRequestToTransferListPanel(result[inx]);
					}
					haksvn.block.off();
					_gTransferGroupChanged = false;
				});
	};
	
	function addRequestToTransferList(){
		var checkedRequestList = $('#tbl_approvedRequestList input[type="checkbox"]:checked');
		if(checkedRequestList.length < 1 ) return; 
		
		$(checkedRequestList).each(function(){
			var transfer = $(this).parent().parent().data("transfer");
			addRequestToTransferListPanel(transfer);
		});
	};
	
	function addRequestToTransferListPanel(transfer){
		var elemId = "div_requestDetail_" + transfer.transferSeq;
		if( $("#"+elemId).length > 0 ){
			$( "#div_duplicateMessage" ).find(".transferSeq").text("req-" + transfer.transferSeq);
			$( "#div_duplicateMessage" ).dialog({
		      	modal: true,
		      	title:'Duplicate Request',
		      	resizable:false,
		      	buttons: {
		        	Ok: function() {
		          		$( this ).dialog( "close" );
		        	}
		      	}
		    });
		}else{
			var requestDetail = $('#div_requestDetail').clone().removeClass('display-none').attr("id",elemId);
			$(requestDetail).attr("transferSeq",transfer.transferSeq);
			$(requestDetail).find(".transferSeq font a").text("req-"+transfer.transferSeq);
			$(requestDetail).find(".transferSeq a").attr("href",'<c:url value="/transfer/request/list"/>' + '/' + transfer.repositorySeq + '/' +  transfer.transferSeq);
			$(requestDetail).find(".requestor").text(transfer.requestUser.userName);
			$(requestDetail).find(".approver").text(transfer.approveUser.userName);
			$(requestDetail).find(".description").text(transfer.description);
			$("#spn_requestsToTran").append(requestDetail);
			changeCurrentTransferListCount(1);
			validTransferGroupForm.form();
			
			$(requestDetail).find(".toggle").click(function(event){
				toggleRequestDetail(this);
			});
			$(requestDetail).find("font.remove a").click(function(event){
				event.stopPropagation();
				removeRequestFromTransferListPanel($(this).parent().parent().parent().parent());
				changeCurrentTransferListCount(-1);
			});
			$(requestDetail).find("font.cancel a").click(function(event){
				event.stopPropagation();
				removeCancelRequestFromTransferListPanel($(this).parent().parent().parent().parent());
				changeCurrentTransferListCount(1);
			});
		}
	};
	
	function changeCurrentTransferListCount(change){
		var curCnt = $("#frm_transferGroup input[name=\"transferListCount\"]").val();
		$("#frm_transferGroup input[name=\"transferListCount\"]").val(Number(curCnt) + change).change();
	};
	
	function removeRequestFromTransferListPanel(transferDetailPanel){
		$(transferDetailPanel).addClass("removed").find(".transferSeq font").addClass("strike")
			.parent().parent().find("font.default").addClass("strike")
			.parent().parent().find("font.remove").addClass("display-none")
			.parent().find("font.cancel").removeClass("display-none");
		
	};
	
	function removeCancelRequestFromTransferListPanel(transferDetailPanel){
		$(transferDetailPanel).removeClass("removed").find(".transferSeq font").removeClass("strike")
			.parent().parent().find("font.default").removeClass("strike")
			.parent().parent().find("font.remove").removeClass("display-none")
			.parent().find("font.cancel").addClass("display-none");
	};
	
	function toggleRequestDetail(pmOpenerParent){
		var pmOpener = $(pmOpenerParent).children('a.pmOpener');
		var div = $(pmOpener).parent().next('div');
		if($(pmOpener).hasClass('opened')){
			$(pmOpener).removeClass('opened').addClass('closed');
			$(div).addClass('display-none');
		}else{
			$(pmOpener).removeClass('closed').addClass('opened');
			$(div).removeClass('display-none');
			if( !$(div).find(".requestSourceList").hasClass('loaded')){
				$(div).find(".requestSourceList").addClass('loading');
				retrieveTransferSourceList($(div).find(".requestSourceList"), $(pmOpenerParent).parent().attr("transferSeq"));
			};
		};
	};
	
	function retrieveTransferSourceList(loadingElem, transferSeq){
		$.getJSON(
				"<c:url value="/transfer/request/list/${repositorySeq}/"/>" + transferSeq + "/sources",
				{},
	            function(result){
					$(loadingElem).removeClass('loading').addClass('loaded');
					for( var inx = 0 ; inx < result.length ; inx++){
						var sourceElem = $(loadingElem).find(".sample").clone().removeClass("sample display-none");
						$(sourceElem).find("a").attr("href", ("<c:url value="/source/browse/${repositorySeq}/" />" + result[inx].path + '?rev=' + result[inx].revision).replace("//", "/"));
						$(sourceElem).find("font a").text(result[inx].path);
						$(sourceElem).find(".type").text(result[inx].transferSourceTypeCode.codeName);
						$(loadingElem).append(sourceElem);
					}
				});
	};
	
	function expandAllTransferDetail(){
		$('#spn_requestsToTran a.pmOpener.closed').trigger('click');
	};
	
	function collapseAllTransferDetail(){
		$('#spn_requestsToTran a.pmOpener.opened').trigger('click');
	};
	
</script>
<div id="table" class="help">
	<h1>Transfer Group Information</h1>
	<div class="col w10 last">
		<div class="content">
		
			<form:form commandName="transferGroup" class="w200" id="frm_transferGroup" method="post" modelAttribute="transferGroup" >
				<c:if test="${transferGroup.transferGroupSeq < 1 }" var="isNewTransferGroup" />
				<p><span class="strong">Detail</span></p>
				<p>
					<form:label path="transferGroupSeq" class="left">Transfer Group Seq</form:label>
					<form:input class="text w_20 readOnly ${isNewTransferGroup ?'visible-hidden':''}" path="transferGroupSeq" readonly="true"/>
				</p>
				<p>
					<form:label path="repositorySeq" class="left">Repository</form:label>
					<form:select path="repositorySeq" disabled="true" items="${repositoryList}" itemValue="repositorySeq" itemLabel="repositoryName"/>
				</p>
				<p>
					<form:label path="transferGroupTypeCode.codeId" class="left">Type</form:label>
					<form:select path="transferGroupTypeCode.codeId" disabled="${not transferGroupStateAuth.isEditable}" items="${requestScope['transfergroup.type.code']}" itemValue="codeId" itemLabel="codeName"/>
				</p>
				<p>
					<form:label path="transferGroupStateCode.codeId" class="left">State</form:label>
					<form:select path="transferGroupStateCode.codeId" disabled="true" items="${requestScope['transfergroup.state.code']}" itemValue="codeId" itemLabel="codeName"/>
				</p>
				<p>
					<form:label path="title" class="left">Title</form:label>
					<form:input class="text w_30" disabled="${not transferGroupStateAuth.isEditable}" path="title"/>
					<form:errors path="title" />
					<span class="status"></span>
				</p>
				<p>
					<form:label path="description" class="left">Description</form:label>
					<form:textarea class="text" disabled="${not transferGroupStateAuth.isEditable}" cols="50" rows="5" path="description"/>
					<form:errors path="description" />
					<span class="status"></span>
				</p>
				<p>
					<form:hidden path="transferUser.userId" />
					<form:hidden path="transferUser.userName" />
					<label class="left">Transfer User</label>
					<input type="text" class="text w_30 readOnly" readonly value="${transferGroup.transferUser.userName}(${transferGroup.transferUser.userId})"/>
				</p>
				<p>
					<form:hidden path="transferDate" />
					<label class="left">Transfer Date</label>
					<input type="text" class="text w_30 readOnly transferDate" readonly/>
				</p>
				<hr/>
				<p>
					<span class="strong">Transfer List</span>
				</p>
				<p>
					<label class="left">Requests for Transfer</label>
					<c:if test="${transferGroupStateAuth.isEditable}">
						<span class="italic"><font class="path"><a onclick="openSearchApprovedRequestDialog()">Add</a></font></span>
					</c:if>
					<span class="italic" style="margin-left:20px;"><font class="path"><a onclick="expandAllTransferDetail()">expand all</a></font></span>
					<span class="italic"><font class="path"><a onclick="collapseAllTransferDetail()">collapse all</a></font></span>
					<input type="text" name="transferListCount" readonly class="text readOnly w_10" style="text-align:right;" value="${fn:length(requestGroup.transferList)}"/> requests
					<span class="status"></span>
					<span id="spn_requestsToTran" style="display:block;margin-left:220px;">
					</span>
				</p>
				<p>
					<label class="left"></label>
					<c:if test="${transferGroupStateAuth.isEditable}">
						<a class="button green mt ml" onclick="saveTransferGroup()"><small class="icon save"></small><span>Save</span></a>
						<script type="text/javascript" >
							function saveTransferGroup(){
								var requestCnt = 0;
								$("#frm_transferGroup input[name^='transferList[']").remove();
								$("#spn_requestsToTran .requestDetail-panel").not(".removed").each(function(){
									$('#frm_transferGroup').append("<input type=\"hidden\" name=\"transferList[" + (requestCnt++) + "].transferSeq\" value=\"" + $(this).attr("transferSeq") + "\" />");
								});
								
								$('#frm_transferGroup').attr('action', '<c:url value="/transfer/requestGroup/list" />' + '<c:out value="/${repositorySeq}/save"/>');
								$('#frm_transferGroup').submit();
							};
						</script>
						<c:if test="${!isNewTransferGroup}">
							<a class="button green mt ml" onclick="transferTransferGroup()"><small class="icon check"></small><span>Transfer</span></a>
							<script type="text/javascript" >
								$(function() {
									$("#frm_transferGroup input[name='title']").change(function(){
										_gTransferGroupChanged = true;
									});
									$("#frm_transferGroup textarea[name='description']").change(function(){
										_gTransferGroupChanged = true;
									});
									$("#frm_transferGroup input[name='transferListCount']").change(function(){
										_gTransferGroupChanged = true;
									});
								});
								
								function transferTransferGroup(){
									if( _gTransferGroupChanged ){
										$("#div_changeOccurMessage").dialog({
											title:'Changes occured',
									      	resizable:false,
									      	width:300,
										    modal: true,
										    buttons: {
										    	"Yes": function(){
										    		_gTransferGroupChanged = false;
										    		transferTransferGroup();
											    },
										    	"No": function() {
										            $( this ).dialog( "close" );
										            return;
										        }
										    }
									    });
									}else{
										haksvn.block.on();
										var queryString = $('#frm_transferGroup').serialize();
										$.post('<c:url value="/transfer/requestGroup/list" />' + '<c:out value="/${repositorySeq}/transfer"/>',
											queryString,
								            function(data){
											haksvn.block.off();
											if( data.type != 'success'){
												$().Message({type:data.type,text:data.text});
												return;
											}
											$("#div_transferSuccessMessage").dialog({
												title:'Transfer finished',
										      	resizable:false,
										      	width:300,
											    modal: true,
											    buttons: {
											    	"Yes": function(){
											    		location.href = "<c:url value="/transfer/tagging/list/${repositorySeq}/add"/>";
												    },
											    	"No": function() {
											    		var sCode = "<spring:eval expression="T(com.haks.haksvn.common.code.util.CodeUtils).getTransferTransferedCodeId()"/>";
											    		location.href = "<c:url value="/transfer/requestGroup/list/${repositorySeq}"/>" + "?sCode=" + sCode;
											        }
											    }
										    });
								        },"json");
									}
								};
							</script>
							<a class="button red mt ml" onclick="deleteTransferGroup()"><small class="icon cross"></small><span>Delete</span></a>
							<script type="text/javascript" >
								function deleteTransferGroup(){
									$('#frm_transferGroup').attr('action', '<c:url value="/transfer/requestGroup/list" />' + '<c:out value="/${repositorySeq}/delete"/>');
									$('#frm_transferGroup').submit();
								};
							</script>
						</c:if>
					</c:if>
					<a class="button yellow mt ml" onclick="history.back()"><small class="icon play"></small><span>Back to List</span></a>
				</p>
			</form:form>
		</div>
	</div>
	<div class="clear"></div>
</div>


<div id="div_searchApprovedRequest" title="Search Approved Request" style="display:none;">
	<div class="module text">
		<div>
			<table id="tbl_approvedRequestList" class="compact">
				<thead>
					<tr>
						<th class="checkbox w_20"></th>
						<th class="w_50">Seq</th>
						<th class="w_80">Requestor</th>
						<th class="w_80">Approver</th>
						<th>Description</th>
					</tr>
				</thead>
				<tbody>
					<tr class="sample">
						<td class="checkbox"><input type="checkbox"/></td>
						<td class="transferSeq"><font class="path open-window"><a></a></font></td>
						<td class="requestor"></td>
						<td class="approver"></td>
						<td class="description"></td>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="5" style="text-align:center;">
							<span class="showmore display-none"><font class="path"><a onclick="retrieveApprovedRequestList()">Show More</a></font></span>
							<span class="loader display-none"><img src="<c:url value="/images/ajax-loader.gif"/>" /></span>
							<span class="nodata">no data</span>
						</td>
					</tr>
				</tfoot>
			</table>
	  	</div>
	</div>
</div>


<div id="div_requestDetail" class="display-none requestDetail-panel">
	<span class="toggle">
		<a class="pmOpener closed"><img class="pClosed" src="<c:url value="/images/plus_small_white.png"/>"/><img class="mOpened" src="<c:url value="/images/minus_small_white.png"/>"/></a>
		<span>
			<span class="transferSeq">
				<font class="path font12 open-window"><a></a></font>
			</span>
			<font class="default">requested by <b class="requestor"></b>, approved by <b class="approver"></b></font>
			<font class="path italic remove"><a>Remove</a></font>
			<font class="path italic cancel display-none"><a>Cancel</a></font>
		</span>
	</span>
	
	<div class="box display-none requestDetail">
		<div class="head"><div></div></div>
		<div class="desc">
			<pre class="description"></pre>
			<ul class="requestSourceList">
				<li class="sample display-none"><span class="type"></span><font class="path open-window"><a></a></font></li>
			</ul>
		</div>
		<div class="bottom"><div></div></div>
	</div>
</div>

<div id="div_duplicateMessage" style="display:none;">
	<p>
    	<span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 50px 0;"></span>
    	<span><b class="transferSeq"></b> is already in the list.</span>
  	</p>
</div>

<div id="div_changeOccurMessage" style="display:none;">
	<p>
    	<span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 50px 0;"></span>
    	<span>There are some changes.</span>
  	</p>
  	<p>
  		If transfer without saving, changes will be lost. Proceed?
  	</p>
</div>

<div id="div_transferSuccessMessage" style="display:none;">
	<p>
    	<span class="ui-icon ui-icon-circle-check" style="float: left; margin: 0 7px 50px 0;"></span>
    	<span>Transfer finished successfully.</span>
  	</p>
  	<p>
  		Do you want to create tagging from current branch?
  	</p>
</div>
