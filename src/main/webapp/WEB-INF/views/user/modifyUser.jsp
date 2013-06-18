<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		var pathname = window.location.pathname;
		actionUrl = pathname.replace('/save','') + '/save';
		$('#frm_user').attr('action', actionUrl);
   	});
	
	function deleteUser(){
		$('#frm_user').attr('action', $('#frm_user').attr('action').replace('save','delete'));
		frm_user.submit();
	};
	
	function setFormValidation(){
		validTaggingForm = $("#frm_user").validate({
			rules: {
				userId:{
					required: true,
					minlength: 4,
					maxlength: 20
				},
				userName: {
					required: true,
					minlength: 4,
					maxlength: 50
				},
				email: {
					required: true,
					email: true
				},
				password: {
					required: true,
					minlength: 4,
					maxlength: 50
				}
			},
			messages: {
				tagName: {
					required: "<spring:message code="validation.required" arguments="Tag Name" />",
					minlength: "<spring:message code="validation.minlength" arguments="6" />",
					maxlength: "<spring:message code="validation.maxlength" arguments="30" />"
				},
				description: {
					required: "<spring:message code="validation.required" arguments="Description" />",
					minlength: "<spring:message code="validation.minlength" arguments="10" />",
					maxlength: "<spring:message code="validation.maxlength" arguments="500" />"
				}
			}
		});
	};
	
</script>
<div id="table" class="help">
	<h1>User Information</h1>
	<div class="col w10 last">
		<div class="content">
			<form:form commandName="user" class="w200" id="frm_user" method="post">
					<form:hidden path="userSeq" name="userSeq"/>
					<c:if test="${user.userSeq lt 1}" var="isNewUser" />
				<p>
					<form:label path="userId" class="left">User ID</form:label>
					<c:choose>
						<c:when test="${isNewUser}">
							<form:input class="text w_10" path="userId"/>
						</c:when>
						<c:otherwise>
							<form:input class="text w_10" path="userId" readonly="true" />
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
					<form:label path="authType" class="left">User Authority</form:label>
					<form:select path="authType" items="${requestScope['user.auth.type.code']}" itemValue="codeId" itemLabel="codeName"/>
				</p>
				<p>
					<form:label path="active" class="left">Active</form:label>
					<form:select path="active" items="${requestScope['common.boolean.yn.code']}" itemValue="codeId" itemLabel="codeName"/>
				</p>
				<p>
					<label class="left"></label>
					<a class="button green mt ml form_submit"><small class="icon check"></small><span>Confirm</span></a>
					<c:if test="${not isNewUser}">
						<a class="button red mt ml" onclick="deleteUser()"><small class="icon cross"></small><span>Delete</span></a>
					</c:if>
				</p>
			</form:form>
		</div>
	</div>
	<div class="clear"></div>
</div>