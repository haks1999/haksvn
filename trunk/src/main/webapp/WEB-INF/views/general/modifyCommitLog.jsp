<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		retrieveCommitLogTemplate();
		$("#sel_repository").change(retrieveCommitLogTemplate);
		$("#sel_logType").change(retrieveCommitLogTemplate);
		
   	});
	
	function retrieveCommitLogTemplate(){
		haksvn.block.on();
		var repositorySeq = $("#sel_repository > option:selected").val();
		$.getJSON( "<c:url value="/configuration/general/commitLog"/>" + "/" + repositorySeq,
				{logType:$("#sel_logType > option:selected").val()},
				function(data) {
					haksvn.block.off();
					$("#commitLogTemplate").text(data.template);
		});
	};
	
	function retrieveDefaultCommitLogTemplate(){
		haksvn.block.on();
		$.getJSON( "<c:url value="/configuration/general/commitLog/default"/>",
				{logType:$("#sel_logType > option:selected").val()},
				function(data) {
					haksvn.block.off();
					$("#commitLogTemplate").text(data.template);
		});
	};
	
	function saveCommitLogTemplate(){
		haksvn.block.on();
		var repositorySeq = $("#sel_repository > option:selected").val();
		$.post("<c:url value="/configuration/general/commitLog"/>" + "/" + repositorySeq,
			{repositorySeq:repositorySeq,
				template:$("#commitLogTemplate").text(),
				logType:$("#sel_logType > option:selected").val()},
	        function(data){
				haksvn.block.off();
				$().Message({type:data.type,text:data.text});
				
	    },"json");
	};
	
	
</script>
<div class="content-page">
	<h1></h1>
	<div class="col w10 last">
		<div class="content">
		
			<div class="box">
				<div class="head"><div></div></div>
				<div class="desc search">
					<p>
						<label>Repository Name</label> 
						<select id="sel_repository">
							<c:forEach items="${repositoryList}" var="repository">
								<option value="<c:out value="${repository.repositorySeq}"/>">
									<c:out value="${repository.repositoryName}" />
								</option>
							</c:forEach>
						</select>
						<label>Log Type</label>
						<haksvn:select id="sel_logType" name="logType" codeGroup="log.template.type.code" ></haksvn:select>
					</p>
				</div>
				<div class="bottom"><div></div></div>
			</div>
			
			<form>
				<p>
					<label for="commitLogTemplate" class="left">Log Template</label>
					<textarea class="text" id="commitLogTemplate" rows="20" cols="80"></textarea>
					<span class="form-help"><spring:message htmlEscape="true" code="helper.general.commitLogTemplateVariables" /></span>
				</p>
				<p>
					<label class="left"></label>
					<a class="button mt yellow " ><small class="icon settings"></small><span onclick="retrieveDefaultCommitLogTemplate()">Get Default</span></a>
					<a class="button green mt ml"><small class="icon check"></small><span onclick="saveCommitLogTemplate()">Confirm</span></a>
				</p>
			</form>
		</div>
	</div>
	<div class="clear"></div>
</div>