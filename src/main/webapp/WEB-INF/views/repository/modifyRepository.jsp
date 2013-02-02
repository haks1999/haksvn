<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		//$('#repositoryForm').validate();
		var pathname = window.location.pathname;
		actionUrl = pathname.replace('/save','') + '/save';
		$('#frm_repository').attr('action', actionUrl);
   	});
	
	function testConnection(){
		
		$.blockUI({ css: { 
            border: 'none', 
            padding: '15px', 
            backgroundColor: '#000', 
            '-webkit-border-radius': '10px', 
            '-moz-border-radius': '10px', 
            opacity: .5, 
            color: '#fff' 
        } }); 
		
		var queryString = $('#frm_repository').serialize();
		$.post("<c:url value="/configuration/repositories/testConnection"/>",
			queryString,
            function(data){
				$.unblockUI();
				$().Message({type:data.type,time:10000,text:data.text,target:"#div_repositoryMessage",click:false}); 
        },"json");
		
		//$().Message({type:'error',time:10000,text:"Some text",target:"#div_repositoryMessage",click:false}); 
	}
</script>
<div id="table" class="help">
	<h1>Repository Information</h1>
	<div class="col w10 last">
		<div class="content">
			<div id="div_repositoryMessage"></div>
			<form:form commandName="repository" class="w200" id="frm_repository" method="post">
					<form:hidden path="repositorySeq" name="repositorySeq"/>
				<p>
					<form:label path="repositoryName" class="left">Repository Name</form:label>
					<form:input class="text w_20" path="repositoryName"/>
					<form:errors path="repositoryName" />
				</p>
				<p>
					<form:label path="repositoryLocation" class="left">Repository Location</form:label>
					<form:input class="text w_30" path="repositoryLocation" />
					<form:errors path="repositoryLocation" cssClass="field_error"/>
				</p>
				<p>
					<form:label path="authUserId" class="left">Repository User ID</form:label>
					<form:input class="text w_10" path="authUserId"/>
					<form:errors path="authUserId" />
				</p>
				<p>
					<form:label path="authUserPasswd" class="left">Repository User Password</form:label>
					<form:input class="text w_10" path="authUserPasswd"/>
					<form:errors path="authUserPasswd" />
				</p>
				<p>
					<form:label path="trunkPath" class="left">Trunk Path</form:label>
					<form:input class="text w_20" path="trunkPath"/>
					(default: /trunk)
					<form:errors path="trunkPath" />
				</p>
				<p>
					<form:label path="tagsPath" class="left">Tags Path</form:label>
					<form:input class="text w_20" path="tagsPath"/>
					(default: /tags)
					<form:errors path="tagsPath" />
				</p>
				<p>
					<form:label path="active" class="left">Active</form:label>
					<form:select path="active" items="${common_boolean_yn_code}" itemValue="codeValue" itemLabel="codeName"/>
				</p>
				<p>
					<label class="left"></label>
					<a class="button mt yellow " ><small class="icon settings"></small><span onclick="testConnection()">Test Connection</span></a>
					<a class="button green mt ml form_submit"><small class="icon check"></small><span>Confirm</span></a>
					<a class="button red mt ml"><small class="icon cross"></small><span>Cancel</span></a>
				</p>
			</form:form>
		</div>
	</div>
	<div class="clear"></div>
</div>