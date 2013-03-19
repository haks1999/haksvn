<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		//$('#repositoryForm').validate();
		
		$('#frm_transfer').attr('action', '<c:url value="/transfer/request/list" />' + '<c:out value="/${repositorySeq}/save"/>');
   	});
	
	
</script>
<div id="table" class="help">
	<h1>Transfer Information</h1>
	<div class="col w10 last">
		<div class="content">
		
			<form:form commandName="transfer" class="w200" id="frm_transfer" method="post">
				<p><span class="strong">Detail</span></p>
				<p>
					<form:label path="transferSeq" class="left">Transfer Seq</form:label>
					<form:input class="text w_20 readOnly ${mode_create?'visible-hidden':''}" path="transferSeq" readonly="true"/>
				</p>
				<p>
					<form:label path="repositorySeq" class="left">Repository</form:label>
					<form:select path="repositorySeq" disabled="${not mode_create}" items="${repositoryList}" itemValue="repositorySeq" itemLabel="repositoryName"/>
				</p>
				<p>
					<form:label path="transferTypeCode.codeId" class="left">Type</form:label>
					<form:select path="transferTypeCode.codeId" items="${requestScope['transfer.type.code']}" itemValue="codeId" itemLabel="codeName"/>
				</p>
				<p>
					<form:label path="transferStateCode.codeId" class="left">State</form:label>
					<form:select path="transferStateCode.codeId" disabled="true" items="${requestScope['transfer.state.code']}" itemValue="codeId" itemLabel="codeName"/>
				</p>
				<p>
					<form:label path="description" class="left">Description</form:label>
					<form:textarea class="text" cols="50" rows="5" path="description"/>
					<form:errors path="description" />
				</p>
				<p>
					<form:label path="requestUser.userId" class="left">Request User Id</form:label>
					<form:input class="text w_30 readOnly" path="requestUser.userId" readonly="true"/>
				</p>
				<p>
					<form:label path="requestDate" class="left">Request Date</form:label>
					<form:input class="text w_30 readOnly ${mode_create?'visible-hidden':''}" path="requestDate" readonly="true"/>
				</p>
				<p>
					<form:label path="transferUser.userId" class="left">Transfer User Id</form:label>
					<form:input class="text w_30 readOnly ${mode_create?'visible-hidden':''}" path="transferUser.userId" readonly="true"/>
				</p>
				<p>
					<form:label path="transferDate" class="left">Transfer Date</form:label>
					<form:input class="text w_30 readOnly ${mode_create?'visible-hidden':''}" path="transferDate" readonly="true"/>
				</p>
				<hr>
				<p><span class="strong">Sources</span></p>
				<p>
					<label class="left"></label>
					<a class="button green mt ml form_submit"><small class="icon check"></small><span>Save</span></a>
				</p>
			</form:form>
		</div>
	</div>
	<div class="clear"></div>
</div>
