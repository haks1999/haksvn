<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		var pathname = window.location.pathname;
		actionUrl = pathname.replace('/save','') + '/save';
		$('#frm_repository').attr('action', actionUrl);
		setFormValidation();
		
		$('#frm_repository').bind('submit',function(){
			if( !validRepositoryForm.valid() ) return;
			if( !isValidForm ) confirmationConnection();
			return isValidForm;
		});
   	});
	
	function testConnection(){
		if( !validRepositoryForm.form() ) return;
		haksvn.block.on();
		var queryString = $('#frm_repository').serialize();
		$.post("<c:url value="/configuration/repositories/list/testConnection"/>",
			queryString,
            function(data){
				haksvn.block.off();
				$().Message({type:data.type,text:data.text});
        },"json");
	};
	
	var isValidForm = false;
	function confirmationConnection(){
		haksvn.block.on();
		var queryString = $('#frm_repository').serialize();
		$.post("<c:url value="/configuration/repositories/list/testConnection"/>",
			queryString,
            function(data){
				haksvn.block.off();
				isValidForm = data.success;
				if(isValidForm){
					haksvn.block.on();
					$('#frm_repository').submit();
				}else{
					$().Message({type:data.type,text:data.text});
				}
        },"json");
	};
	
	function selectSynchrozingUser( selection ){
		if(selection){
			$('#div_serverSettings').slideDown();
			frm_repository.syncUser.value = 'common.boolean.yn.code.y';
			frm_repository.connectType.value = 'server.connect.type.code.local';
		}else{
			$('#div_serverSettings').slideUp();
			frm_repository.syncUser.value = 'common.boolean.yn.code.n';
			frm_repository.connectType.value = '';
		}
	}
	
	function changeServerConnectType( connectType ){
		if( connectType == 'server.connect.type.code.local' ){
			$('#div_serverRemoteSettings').slideUp();
			$("#frm_repository input[name=authzPath]").rules("remove");
			$("#frm_repository input[name=passwdPath]").rules("remove");
		}else{
			$('#div_serverRemoteSettings').slideDown();
			$("#frm_repository input[name=authzPath]").rules("add", {required:true});
			$("#frm_repository input[name=passwdPath]").rules("add", {required:true});
		}
	};
	
	function openAuthzTemplateDialog(){
		$('#txa_authzTemplate').val(frm_repository.authzTemplate.value);
		$("#div_authzTemplate").dialog({
		    width: 550,
		    modal: true,
		    buttons: {
				"Restore Default": function() {
					$('#txa_authzTemplate').val($('#txa_authzDefaultTemplate').val());
	          	},
		        "Confirm": function() {
		          	frm_repository.authzTemplate.value = $('#txa_authzTemplate').val();
		           	$( this ).dialog( "close" );
		         },
		        Cancel: function() {
		           	$( this ).dialog( "close" );
		        }
		    }
	    });
	};
	
	function deleteRepository(){
		$('#frm_repository').attr('action', $('#frm_repository').attr('action').replace('save','delete'));
		frm_repository.submit();
	};
	
	var validRepositoryForm;
	function setFormValidation(){
		validRepositoryForm = $("#frm_repository").validate({
			rules: {
				repositoryName:{
					required: true,
					minlength: 5,
					maxlength: 50
				},
				repositoryLocation: {
					required: true
				},
				authUserId: {
					required: true
				},
				authUserPasswd: {
					required: true
				},
				trunkPath:{
					required: true,
					svnpath: true
				},
				tagsPath:{
					required: true,
					svnpath: true
				},
				branchesPath:{
					required: true,
					svnpath: true
				},
				authzPath:{
					required: true
				},
				passwdPath:{
					required: true
				}
			}
		});
	};
	
</script>
<div class="content-page">
	<h1>Repository Information</h1>
	<div class="col w10 last">
		<div class="content">
			<form:form commandName="repository" class="w200" id="frm_repository" method="post">
				<p><span class="strong">Repository Settings</span></p>
				<p>
					<form:label path="repositoryName" class="left">Repository Name</form:label>
					<form:input class="text w_20" path="repositoryName"/>
					<form:errors path="repositoryName" />
					<span class="form-status"></span>
				</p>
				<p>
					<c:if test="${empty repository.repositoryKey}" var="isNewRepository" />
					<form:label path="repositoryKey" class="left">Repository Key</form:label>
					<c:choose>
						<c:when test="${isNewRepository}">
							<form:input class="text w_10" path="repositoryKey"/>
							<script type="text/javascript">
								$(function() {
									$("#frm_repository input[name=repositoryKey]").rules("add", {
										required: true,
										minlength: 5,
										maxlength: 15,
										capital: true,
										remote:{
						                      url: "<c:url value="/configuration/repositories/add/validateRepositoryKey/"/>",
						                      data:{
						                          repositoryKey: function(){
						                              return $("#frm_repository input[name=repositoryKey]").val();
						                          }
						                      }
						                    },
										messages:{
											remote: $.validator.format("<spring:message code="validation.duplicate" />")
										}
									});
								});
							</script>
						</c:when>
						<c:otherwise>
							<form:input class="text w_10 readOnly" path="repositoryKey" readonly="true"/>
						</c:otherwise>
					</c:choose>
					<form:errors path="repositoryKey" />
					<span class="form-help"><spring:message code="helper.repository.repositoryKey" /></span>
					<span class="form-status"></span>
				</p>
				
				<p>
					<form:label path="repositoryLocation" class="left">Repository Location</form:label>
					<form:input class="text w_30" path="repositoryLocation" />
					<form:errors path="repositoryLocation" />
					<span class="form-help"><spring:message code="helper.repository.location" /></span>
					<span class="form-status"></span>
				</p>
				<p>
					<form:label path="authUserId" class="left">Repository User ID</form:label>
					<form:input class="text w_10" path="authUserId"/>
					<form:errors path="authUserId" />
					<span class="form-status"></span>
				</p>
				<p>
					<form:label path="authUserPasswd" class="left">Repository User Password</form:label>
					<form:password class="text w_10" path="authUserPasswd"/>
					<form:errors path="authUserPasswd" />
					<span class="form-status"></span>
				</p>
				<p>
					<form:label path="trunkPath" class="left">Trunk Path</form:label>
					<form:input class="text w_20" path="trunkPath" value="${isNewRepository? '/trunk':repository.trunkPath}"/>
					<span class="form-help"><spring:message code="helper.repository.trunkPath" /></span>
					<form:errors path="trunkPath" />
					<span class="form-status"></span>
				</p>
				<p>
					<form:label path="tagsPath" class="left">Tags Path</form:label>
					<form:input class="text w_20" path="tagsPath" value="${isNewRepository? '/tags':repository.tagsPath}"/>
					<span class="form-help"><spring:message code="helper.repository.tagsPath" /></span>
					<form:errors path="tagsPath" />
					<span class="form-status"></span>
				</p>
				<p>
					<form:label path="branchesPath" class="left">Production Branch Path</form:label>
					<form:input class="text w_20" path="branchesPath" value="${isNewRepository? '/branches/production':repository.branchesPath}"/>
					<span class="form-help"><spring:message code="helper.repository.branchesPath" /></span>
					<form:errors path="branchesPath" />
					<span class="form-status"></span>
				</p>
				<p>
					<form:label path="active" class="left">Active</form:label>
					<form:select path="active" items="${requestScope['common.boolean.yn.code']}" itemValue="codeId" itemLabel="codeName"/>
				</p>
				<p>
					<c:if test="${repository.syncUser eq 'common.boolean.yn.code.y'}" var="syncUserYn" />
					<label for="ckb_syncUser" class="left">Synchronize User</label>
					<input id="ckb_syncUser" type="checkbox" class="plaincheckbox" ${syncUserYn? "checked":""} onclick="selectSynchrozingUser(this.checked)"/>
					<input type="text" disabled class="text visible-hidden" style="width:1px;"/>
					<span class="form-help"><spring:message code="helper.repository.syncUser" /></span>
					<form:hidden path="syncUser" />
				</p>
				<div id="div_serverSettings" style="${syncUserYn? '' : 'display:none;'}">
					<hr>
					<p><span class="strong">Server Settings</span></p>
					<p>
						<form:label path="connectType" class="left">Connection Type</form:label>
						<form:select path="connectType" items="${requestScope['server.connect.type.code']}" itemValue="codeId" itemLabel="codeName" onchange="changeServerConnectType(this.value)"/>
					</p>
					<div id="div_serverRemoteSettings" style="${ (repository.connectType eq 'server.connect.type.code.local')||(empty repository.connectType)? 'display:none;' : ''}">
						<p>
							<form:label path="serverIp" class="left">Server address</form:label>
							<form:input path="serverIp" class="text w_20" />
						</p>
						<p>
							<form:label path="serverUserId" class="left">Server User ID</form:label>
							<form:input path="serverUserId" class="text w_10" />
						</p>
						<p>
							<form:label path="serverUserPasswd" class="left">Server User Password</form:label>
							<form:input path="serverUserPasswd" class="text w_10" />
						</p>
					</div>
					<p>
						<form:label path="authzPath" class="left">authz file path</form:label>
						<form:input path="authzPath" class="text w_30" />
						<span class="form-status"></span>
					</p>
					<p>
						<form:label path="authzTemplate" class="left">authz file template</form:label>
						<form:hidden path="authzTemplate" />
						<span class="underline italic link" onclick="openAuthzTemplateDialog()">Edit</span>
						<input type="text" disabled class="text visible-hidden" style="width:1px;"/>
						<span class="form-help"><spring:message htmlEscape="true" code="helper.repository.authzTemplate" /></span>
					</p>
					<p>
						<form:label path="passwdPath" class="left">passwd file path</form:label>
						<form:input path="passwdPath" class="text w_30" />
						<span class="form-status"></span>
					</p>
					<p>
						<form:label path="passwdType" class="left">SVN password type</form:label>
						<form:select path="passwdType" items="${requestScope['svn.passwd.type.code']}" itemValue="codeId" itemLabel="codeName"/>
					</p>
				</div>
				<p>
					<label class="left"></label>
					<a class="button mt yellow " ><small class="icon settings"></small><span onclick="testConnection()">Test Connection</span></a>
					<a class="button green mt ml form_submit"><small class="icon check"></small><span>Confirm</span></a>
					<c:if test="${not isNewRepository}">
						<a class="button red mt ml" onclick="deleteRepository()"><small class="icon cross"></small><span>Delete</span></a>
					</c:if>
				</p>
			</form:form>
		</div>
	</div>
	<div class="clear"></div>
</div>
<div id="div_authzTemplate" title="Authz Template" style="display:none;">
	<div class="info">
		<div class="tl"></div>
		<div class="tr"></div>
		<div class="desc variable-help"><spring:message code="helper.repository.authzTemplateVariables" /></div>
		<div class="bl"></div>
		<div class="br"></div>
	</div>
						
	<div class="content fullsize">
		<textarea id="txa_authzTemplate"><c:out value="${repository.authzTemplate}"/></textarea>
		<textarea id="txa_authzDefaultTemplate" style="display:none;"><c:out value="${authzTemplate}"/></textarea>
	</div>
</div>
