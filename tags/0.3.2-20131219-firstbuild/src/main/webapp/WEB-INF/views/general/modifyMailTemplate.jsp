<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		retrieveMailTemplate();
		$("#sel_repository").change(retrieveMailTemplate);
		$("#sel_template").change(retrieveMailTemplate);
		
   	});
	
	function retrieveMailTemplate(){
		haksvn.block.on();
		var repositoryKey = $("#sel_repository > option:selected").val();
		$.getJSON( "<c:url value="/configuration/general/mailTemplate"/>" + "/" + repositoryKey,
				{template:$("#sel_template > option:selected").val()},
				function(data) {
					haksvn.block.off();
					$("#ipt_subject").val(data.subject);
					$("#txt_text").text(data.text);
		});
	};
	
	function retrieveDefaultMailTemplate(){
		haksvn.block.on();
		$.getJSON( "<c:url value="/configuration/general/mailTemplate/default"/>",
				{template:$("#sel_template > option:selected").val()},
				function(data) {
					haksvn.block.off();
					$("#ipt_subject").val(data.subject);
					$("#txt_text").text(data.text);
		});
	};
	
	function saveMailTemplate(){
		haksvn.block.on();
		var repositoryKey = $("#sel_repository > option:selected").val();
		$.post("<c:url value="/configuration/general/mailTemplate"/>" + "/" + repositoryKey,
			{repositoryKey:repositoryKey,
				subject:$("#ipt_subject").val(),
				text:$("#txt_text").text(),
				template:$("#sel_template > option:selected").val()},
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
								<option value="<c:out value="${repository.repositoryKey}"/>">
									<c:out value="${repository.repositoryName}" />
								</option>
							</c:forEach>
						</select>
						<label>Template</label>
						<haksvn:select id="sel_template" name="template" codeGroup="mail.template.type.code" ></haksvn:select>
					</p>
				</div>
				<div class="bottom"><div></div></div>
			</div>
			
			<form>
				<p>
					<label for="subject" class="left">Subject</label>
					<input type="text" id="ipt_subject" class="text w_500" name="subject"/>
				</p>
				<p>
					<label for="text" class="left">Text</label>
					<textarea class="text" id="txt_text" rows="15" cols="100"></textarea>
					<span class="form-help"><spring:message htmlEscape="true" code="helper.general.mailTemplateVariables" /></span>
				</p>
				<p>
					<label class="left"></label>
					<a class="button mt yellow " ><small class="icon settings"></small><span onclick="retrieveDefaultMailTemplate()">Get Default</span></a>
					<a class="button green mt ml"><small class="icon check"></small><span onclick="saveMailTemplate()">Confirm</span></a>
				</p>
			</form>
		</div>
	</div>
	<div class="clear"></div>
</div>