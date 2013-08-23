<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<style type="text/css">
input[type="radio"] {
  vertical-align: middle;
  font-style: italic;
}
</style>
<script type="text/javascript">
	$(function() {
		$("#frm_mail").attr("action","<c:url value="/configuration/general/mail"/>");
		setFormValidation();
		selectAuthEnabled($("#frm_mail input[name=authEnabled]").attr("checked"));
   	});
	
	function setFormValidation(){
		$("#frm_mail").validate({
			rules: {
				host:{
					required: true
				},
				port: {
					required: true,
					number: true
				},
				replyto: {
					required: true,
					email: true
				}
			}
		});
	};
	
	function selectAuthEnabled( selection ){
		if(selection){
			$('#div_authSettings').slideDown();
			$("#frm_mail input[name=username]").rules("add", {required:true});
			$("#frm_mail input[name=password]").rules("add", {required:true});			
		}else{
			$("#frm_mail input[name=username]").rules("remove");
			$("#frm_mail input[name=password]").rules("remove");
			$('#div_authSettings').slideUp();
		}
	};
</script>
<div class="content-page">
	<div class="col w10 last">
		<div class="content">
			<h1>Mail Configuration</h1>
			<form:form commandName="mailConfiguration" class="w200" id="frm_mail" method="post">
				<p>
					<form:label path="host" class="left">SMTP Host</form:label>
					<form:input class="text w_200" path="host"/>
					<form:errors path="host" />
					<span class="form-status"></span>
				</p>
				<p>
					<form:label path="port" class="left">SMTP Port</form:label>
					<form:input class="text w_10" path="port"/>
					<form:errors path="port" />
					<span class="form-status"></span>
				</p>
				<p>
					<form:label path="replyto" class="left">Reply To Address</form:label>
					<form:input class="text w_200" path="replyto"/>
					<form:errors path="replyto" />
					<span class="form-status"></span>
				</p>
				<p>
					<form:label path="sslEnabled" class="left">Use SSL</form:label>
					<form:checkbox path="sslEnabled" />
					<form:errors path="sslEnabled" />
					<input type="text" class="text visible-hidden"/>
				</p>
				<p>
					<form:label path="authEnabled" class="left">Use SMTP Authentication</form:label>
					<form:checkbox path="authEnabled" onclick="selectAuthEnabled(this.checked)"/>
					<form:errors path="authEnabled" />
					<input type="text" class="text visible-hidden"/>
				</p>
				<div id="div_authSettings" style="${mailConfiguration.authEnabled? '' : 'display:none;'}">
					<p>
						<form:label path="username" class="left">User Name</form:label>
						<form:input class="text w_10" path="username"/>
						<form:errors path="username" />
						<span class="form-status"></span>
					</p>
					<p>
						<form:label path="password" class="left">Password</form:label>
						<form:password class="text w_10" path="password"/>
						<form:errors path="password" />
						<span class="form-status"></span>
					</p>
				</div>
				<p>
					<label class="left"></label>
					<a class="button green mt ml form_submit"><small class="icon check"></small><span>Confirm</span></a>
				</p>
			</form:form>
		</div>
	</div>
	<hr/>
	<div class="col w10 last">
		<div class="content">
			<h1>Mail Notification</h1>
				<form class="w200" method="post" action="<c:url value="/configuration/general/mailNotice" />">
					<c:forEach items="${requestScope['mail.notice.type.code']}" var="mailNotice">
						<p>
							<label class="left"><c:out value="${mailNotice.codeName}"/></label>
							<input type="radio" name="<c:out value="${mailNotice.codeId}"/>"  value="true"  <c:if test="${mailNotice.codeValue}">checked="checked"</c:if> /><span class="text italic">Enabled</span>
							<input type="radio" name="<c:out value="${mailNotice.codeId}"/>"  value="false" <c:if test="${!mailNotice.codeValue}">checked="checked"</c:if> /><span class="text italic">Disabled</span>
							<input type="text" class="text visible-hidden"/>
						</p>
					</c:forEach>
					<p>
						<label class="left"></label>
						<span class="text italic">Notification Detail </span><span class="form-help"><spring:message htmlEscape="true" code="helper.general.mailNoticeType" /></span>
					</p>
					<p>
						<label class="left"></label>
						<a class="button green mt ml form_submit"><small class="icon check"></small><span>Confirm</span></a>
					</p>
				</form>
		</div>
	</div>
	<div class="clear"></div>
</div>