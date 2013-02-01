<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		//$('#repositoryForm').validate();
		$('#frm_user').attr('action', document.location + '/save');
   	});
	
</script>
<div id="table" class="help">
	<h1>User Information</h1>
	<div class="col w10 last">
		<div class="content">
			<form:form commandName="user" class="w200" id="frm_user" method="post">
					<form:hidden path="userSeq" name="userSeq"/>
				<p>
					<form:label path="userId" class="left">User ID</form:label>
					<c:choose>
						<c:when test="${user.userSeq lt 1}">
							<form:input class="text w_10" path="userId"/>
						</c:when>
						<c:otherwise>
							<form:input class="text w_10" path="userId" readonly="true" disabled="true"/>
						</c:otherwise>
					</c:choose>
					<form:errors path="userId" />
				</p>
				<p>
					<form:label path="userName" class="left">User Name</form:label>
					<form:input class="text w_20" path="userName" />
					<form:errors path="userName" />
				</p>
				<p>
					<form:label path="email" class="left">Email</form:label>
					<form:input class="text w_20" path="email"/>
					<form:errors path="email" />
				</p>
				<p>
					<form:label path="userPasswd" class="left">Password</form:label>
					<form:input class="text w_10" path="userPasswd"/>
					<form:errors path="userPasswd" />
				</p>
				<p>
					<form:label path="active" class="left">Active</form:label>
					<form:select path="active" items="${common_boolean_yn_code}" itemValue="codeValue" itemLabel="codeName"/>
				</p>
				<p>
					<label class="left"></label>
					<a class="button green mt ml form_submit"><small class="icon check"></small><span>Confirm</span></a>
					<a class="button red mt ml"><small class="icon cross"></small><span>Cancel</span></a>
				</p>
			</form:form>
		</div>
	</div>
	<div class="clear"></div>
</div>