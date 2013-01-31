<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		
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
		
		var queryString = $('#repositoryForm').serialize();
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
			<c:url var="repositoryFormUrl" value="/configuration/repositories/save"/>
			<form:form commandName="repository" class="w200" id="repositoryForm" method="post" action="${repositoryFormUrl}">
					<form:hidden path="repositorySeq" name="repositorySeq"/>
				<p>
					<form:label path="repositoryName" class="left">Repository Name</form:label>
					<form:input class="text w_20" path="repositoryName"/>
				</p>
				<p>
					<form:label path="repositoryLocation" class="left">Repository Location</form:label>
					<form:input class="text w_30" path="repositoryLocation" />
				</p>
				<p>
					<form:label path="authUserId" class="left">Repository User ID</form:label>
					<form:input class="text w_10" path="authUserId"/>
				</p>
				<p>
					<form:label path="authUserPasswd" class="left">Repository User Password</form:label>
					<form:input class="text w_10" path="authUserPasswd"/>
				</p>
				<p>
					<form:label path="trunkPath" class="left">Trunk Path</form:label>
					<form:input class="text w_20" path="trunkPath"/>
					(default: /trunk)
				</p>
				<p>
					<form:label path="tagsPath" class="left">Tags Path</form:label>
					<form:input class="text w_20" path="tagsPath"/>
					(default: /tags)
				</p>
				<p>
					<form:label path="repositoryStatus" class="left">Repository Status</form:label>
					<form:select path="repositoryStatus" items="${repository_status_code}" itemValue="codeValue" itemLabel="codeName"/>
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