<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/haksvn.tld" prefix="haksvn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<script type="text/javascript">
	$(function() {
		document.repositoryForm.action = '<c:url value="/configuration/repositories/save"/>';
   	});
	
	function testConnection(){
		$().Message({type:'error',time:10000,text:"Some text",target:"#div_repositoryMessage",click:false}); 
	}
</script>
<div id="table" class="help">
	<h1>Repository Information</h1>
	<div class="col w10 last">
		<div class="content">
			<div id="div_repositoryMessage"></div>
			<form:form commandName="repository" class="w200" name="repositoryForm" method="post" action="">
				<c:if test="${not empty repository.repositorySeq}">
					<form:hidden path="repositorySeq" name="repositorySeq"/>
				</c:if>
				
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