<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/haksvn.tld" prefix="haksvn" %>
<script type="text/javascript">
	$(function() {
		
   	});
</script>
<div id="table" class="help">
	<h1>Repository Information</h1>
	<div class="col w10 last">
		<div class="content">
			<form class="w200" name="repositoryForm" method="post" action="<c:url value="/configuration/repositories/save"/>">
				<c:if test="${not empty repository.repositorySeq}">
					<input type="hidden" name="repositorySeq" value="<c:out value="${repository.repositorySeq}" />"/>
				</c:if>
				<p>
					<label for="repositoryName" class="left">Repository Name</label>
					<input type="text" class="text w_20" name="repositoryName" value="<c:out value="${repository.repositoryName}" />"/>
				</p>
				<p>
					<label for="repositoryLocation" class="left">Repository Location</label>
					<input type="text" class="text w_30" name="repositoryLocation" value="<c:out value="${repository.repositoryLocation}" />"/>
				</p>
				<p>
					<label for="authUserId" class="left">Repository User ID</label>
					<input type="text" class="text w_10" name="authUserId" value="<c:out value="${repository.authUserId}" />"/>
				</p>
				<p>
					<label for="authUserPasswd" class="left">Repository User Password</label>
					<input type="text" class="text w_10" name="authUserPasswd" value="<c:out value="${repository.authUserPasswd}" />"/>
				</p>
				<p>
					<label for="trunkPath" class="left">Trunk Path</label>
					<input type="text" class="text w_20" name="trunkPath" value="<c:out value="${repository.trunkPath}" />"/>
					(default: /trunk)
				</p>
				<p>
					<label for="tagsPath" class="left">Tags Path</label>
					<input type="text" class="text w_20" name="tagsPath" value="<c:out value="${repository.tagsPath}" />"/>
					(default: /tags)
				</p>
				<p>
					<label for="select" class="left">Repository Status</label>
					<haksvn:select name="repositoryStatus" codeGroup="repository.status" selectedValue="${repository.repositoryStatus}"></haksvn:select>
				</p>
				<p>
					<label class="left"></label>
					<a class="button mt yellow "><small class="icon settings"></small><span>Test Connection</span></a>
					<a class="button green mt ml form_submit"><small class="icon check"></small><span>Confirm</span></a>
					<a class="button red mt ml"><small class="icon cross"></small><span>Cancel</span></a>
				</p>
			</form>
		</div>
	</div>
	<div class="clear"></div>
</div>