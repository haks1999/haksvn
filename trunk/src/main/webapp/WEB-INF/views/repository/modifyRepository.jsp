<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	$(function() {
		//$('#repositoryForm').validate();
		var pathname = window.location.pathname;
		actionUrl = pathname.replace('/save','') + '/save';
		$('#frm_repository').attr('action', actionUrl);
   	});
	
	function testConnection(){
		ajaxProcessing();
		var queryString = $('#frm_repository').serialize();
		$.post("<c:url value="/configuration/repositories/testConnection"/>",
			queryString,
            function(data){
				$().Message({type:data.type,text:data.text});
				
        },"json");
		
		//$().Message({type:'error',time:10000,text:"Some text",target:"#div_repositoryMessage",click:false}); 
	}
	
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
		}else{
			$('#div_serverRemoteSettings').slideDown();
		}
	};
	
	function openAuthzTemplateDialog(){
		$('#txa_authzTemplate').val(frm_repository.authzTemplate.value);
		$("#div_authzTemplate").dialog({
			height: 500,
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
	
</script>
<div id="table" class="help">
	<h1>Repository Information</h1>
	<div class="col w10 last">
		<div class="content">
		
			<form:form commandName="repository" class="w200" id="frm_repository" method="post">
					<form:hidden path="repositorySeq" name="repositorySeq"/>
				<p><span class="strong">Repository Settings</span></p>
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
					<form:label path="branchesPath" class="left">Branches Path</form:label>
					<form:input class="text w_20" path="branchesPath"/>
					(default: /branches/production)
					<form:errors path="branchesPath" />
				</p>
				<p>
					<form:label path="active" class="left">Active</form:label>
					<form:select path="active" items="${requestScope['common.boolean.yn.code']}" itemValue="codeId" itemLabel="codeName"/>
				</p>
				<p>
					<c:if test="${repository.syncUser eq 'common.boolean.yn.code.y'}" var="syncUserYn" />
					<label for="ckb_syncUser" class="left">Synchronize User</label>
					<input id="ckb_syncUser" type="checkbox" class="plaincheckbox" ${syncUserYn? "checked":""} onclick="selectSynchrozingUser(this.checked)"/>
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
							<form:label path="userId" class="left">User ID</form:label>
							<form:input path="userId" class="text w_10" />
						</p>
						<p>
							<form:label path="userPasswd" class="left">User Password</form:label>
							<form:input path="userPasswd" class="text w_10" />
						</p>
					</div>
					<p>
						<form:label path="authzPath" class="left">authz file path</form:label>
						<form:input path="authzPath" class="text w_30" />
					</p>
					<p>
						<form:label path="authzTemplate" class="left">authz file template</form:label>
						<form:hidden path="authzTemplate" />
						<span class="underline italic link" onclick="openAuthzTemplateDialog()">Edit</span>
					</p>
					<p>
						<form:label path="passwdPath" class="left">passwd file path</form:label>
						<form:input path="passwdPath" class="text w_30" />
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
					<a class="button red mt ml"><small class="icon cross"></small><span>Cancel</span></a>
				</p>
			</form:form>
		</div>
	</div>
	<div class="clear"></div>
</div>
<div id="div_authzTemplate" title="Authz Template" style="display:none;">
	<div class="content fullsize">
		<textarea id="txa_authzTemplate"><c:out value="${repository.authzTemplate}"/></textarea>
		<textarea id="txa_authzDefaultTemplate" style="display:none;"><c:out value="${authzTemplate}"/></textarea>
	</div>
</div>
