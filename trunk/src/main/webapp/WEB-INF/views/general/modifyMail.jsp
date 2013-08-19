<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		$("#frm_mail").attr("action","<c:url value="/configuration/general/mail"/>");
		setFormValidation();
		selectAuthEnabled($("#frm_mail input[name=authEnabled]").val());
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
	<!-- 
	<hr/>
	<div class="col w10 last">
		<div class="content">
			<h1>Mail Notification</h1>
		</div>
	</div>
	-->
	<div class="clear"></div>
</div>