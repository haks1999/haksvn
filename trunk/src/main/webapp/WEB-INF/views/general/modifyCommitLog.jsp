<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		retrieveCommitLogTemplate();
		$("#sel_repository").change(retrieveCommitLogTemplate);
   	});
	
	function retrieveCommitLogTemplate(){
		haksvn.block.on();
		var repositorySeq = $("#sel_repository > option:selected").val();
		$.getJSON( "<c:url value="/configuration/general/commitLog"/>" + "/" + repositorySeq,
				{},
				function(data) {
					haksvn.block.off();
					$("#commitLogTemplate").text(data.template);
		});
	};
	
	function retrieveDefaultCommitLogTemplate(){
		haksvn.block.on();
		$.getJSON( "<c:url value="/configuration/general/commitLog/default"/>",
				{},
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
				template:$("#commitLogTemplate").text()},
	        function(data){
				haksvn.block.off();
				$().Message({type:data.type,text:data.text});
				
	    },"json");
	};
	
	
</script>
<div id="table" class="help">
	<h1></h1>
	<div class="col w10 last">
		<div class="content">
		
			<div class="box">
				<div class="head"><div></div></div>
				<div class="desc">
					<p>
						<label>Repository Name</label> 
						<select id="sel_repository">
							<c:forEach items="${repositoryList}" var="repository">
								<option value="<c:out value="${repository.repositorySeq}"/>">
									<c:out value="${repository.repositoryName}" />
								</option>
							</c:forEach>
						</select>
					</p>
				</div>
				<div class="bottom"><div></div></div>
			</div>
			
			<div style="font-size:11px;padding-bottom:10px;">
				variables: #request-id#, #request-user-id#, #request-user-name#, #approve-user-id#, #approve-user-name#, #description#
			</div>
			
			<form>
				<p>
					<label for="commitLogTemplate" class="left">Log Template</label>
					<textarea class="text" id="commitLogTemplate" rows="20" cols="80"></textarea>
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