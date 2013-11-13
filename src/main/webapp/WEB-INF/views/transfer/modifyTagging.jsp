<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	
	$(function() {
		$('#frm_tagging').attr('action', '<c:url value="/transfer/tagging/list/${repositoryKey}/save" />');
		transformDateField();
		setFormValidation();
   	});
	
	function transformDateField(){
		var taggingDate = Number('<c:out value="${tagging.taggingDate}"/>');
		if( taggingDate > 0 ) $('#frm_tagging input.taggingDate').val(haksvn.date.convertToComplexFullFormat(new Date(taggingDate)));
	};
	
	var validTaggingForm;
	function setFormValidation(){
		validTaggingForm = $("#frm_tagging").validate({
			rules: {
				tagName:{
					required: true,
					minlength: 6,
					maxlength: 30
				},
				description: {
					required: true,
					minlength: 10,
					maxlength: 500
				}
			}
		});
	}
	
	
</script>
<c:set var="repoBrowsePathLink" value="${pageContext.request.contextPath}/source/browse/${repositoryKey}"/>
<c:set var="repoChangesPathLink" value="${pageContext.request.contextPath}/source/changes/${repositoryKey}"/>
<div class="content-page">
	<h1>Tagging Information</h1>
	<div class="col w10 last">
		<div class="content">
			<form:form commandName="tagging" class="w200" id="frm_tagging" method="post">
				<p>
					<form:label path="taggingSeq" class="left">Tagging Seq</form:label>
					<form:input class="text w_120 readOnly ${tagging.taggingSeq < 1?'visible-hidden':''}" path="taggingSeq" readonly="true"/>
				</p>
				<p>
					<form:label path="repositoryKey" class="left">Repository</form:label>
					<form:select path="repositoryKey" disabled="true" items="${repositoryList}" itemValue="repositoryKey" itemLabel="repositoryName"/>
				</p>
				<p>
					<form:label path="taggingTypeCode.codeId" class="left">Type</form:label>
					<form:select path="taggingTypeCode.codeId" disabled="true" items="${requestScope['tagging.type.code']}" itemValue="codeId" itemLabel="codeName"/>
					<span class="form-help"><spring:message htmlEscape="true" code="helper.tagging.type" /></span>
				</p>
				<p>
					<form:label path="srcPath" class="left">Source</form:label>
					<c:if test="${not empty tagging.srcPath}">
						<font class="path open-window"><a href="${repoBrowsePathLink}${tagging.srcPath}"><c:out value="${tagging.srcPath}"/></a></font>
						<font class="path open-window"><a href="${repoChangesPathLink}?rev=${tagging.srcRevision}"><c:out value="r${tagging.srcRevision}"/></a></font>
					</c:if>
					<input type="text" class="text visible-hidden"/>
				</p>
				<p>
					<form:label path="destPath" class="left">Destination</form:label>
					<form:hidden class="text w_200" disabled="true" path="destPath"/>
					<c:if test="${not empty tagging.destPath}">
						<font class="path open-window"><a href="${repoBrowsePathLink}${tagging.destPath}"><c:out value="${tagging.destPath}"/></a></font>
						<font class="path open-window"><a href="${repoChangesPathLink}?rev=${tagging.destRevision}"><c:out value="r${tagging.destRevision}"/></a></font>
					</c:if>
					<input type="text" class="text visible-hidden"/>
				</p>
				<p>
					<form:label path="tagName" class="left">Tag Name</form:label>
					<form:input class="text w_120" disabled="${not taggingAuth.isCreatable}" path="tagName"/>
					<form:errors path="tagName" />
					<span class="form-help"><spring:message htmlEscape="true" code="helper.tagging.name" /></span>
					<span class="form-status"></span>
				</p>
				<p>
					<form:label path="description" class="left">Description</form:label>
					<form:textarea class="text" disabled="${not taggingAuth.isCreatable}" cols="50" rows="5" path="description"/>
					<form:errors path="description" />
					<span class="form-help"><spring:message htmlEscape="true" code="helper.tagging.description" /></span>
					<span class="form-status"></span>
				</p>
				<p>
					<form:hidden path="taggingUser.userId" />
					<form:hidden path="taggingUser.userName" />
					<label class="left">Tagging User</label>
					<input type="text" class="text w_150 readOnly" readonly value="${tagging.taggingUser.userName}(${tagging.taggingUser.userId})"/>
				</p>
				<p>
					<form:hidden path="taggingDate" />
					<label class="left">Tagging Date</label>
					<input type="text" class="text w_150 readOnly taggingDate" readonly/>
				</p>
				<p>
					<label class="left"></label>
					<c:if test="${taggingAuth.isCreatable}">
						<a class="button green mt ml" onclick="createTagging()"><small class="icon plus"></small><span>Create</span></a>
						<script type="text/javascript" >
							function createTagging(){
								$('#frm_tagging').attr('action', '<c:url value="/transfer/tagging/list" />' + '<c:out value="/${repositoryKey}/create"/>');
								$('#frm_tagging').submit();
							};
							
							$(function() {								 
								$('#frm_tagging').bind('submit',function(){
									if( !validTaggingForm.valid() ) return;
									if( !isValidForm ) validateTagging();
									return isValidForm;
								});
						   	});
							
							var isValidForm = false;
							function validateTagging(){
								haksvn.block.on();
								$.ajax( {
									type:'POST',
									url:"<c:url value="/transfer/tagging/list"/>" + "/" + '<c:out value="${repositoryKey}/validate" />',
									data:{
										tagName:$('#frm_tagging input[name="tagName"]').val()
									},
									success: function(data){
										if( !data || !data.taggingSeq ){
											isValidForm = true;
											return;
										}
										haksvn.block.off();
										$('#div_duplicateMessage .tagName').text(data.tagName);
										$('#div_duplicateMessage .taggingSeq').text('tagging-'+data.taggingSeq);
										$('#div_duplicateMessage .taggingUserId').text(data.taggingUser.userName+'('+data.taggingUser.userId+')');
										$('#div_duplicateMessage .taggingDate').text(haksvn.date.convertToComplexFullFormat(new Date(data.taggingDate)));
										$( "#div_duplicateMessage" ).dialog({
									      	modal: true,
									      	width: 350,
									      	title:'Existing Tag Name',
									      	resizable:false,
									      	buttons: {
									      		"Yes": function() {
									          		$( this ).dialog( "close" );
									          		haksvn.block.on();
									          		isValidForm = true;
									          		$('#frm_tagging').submit();
									        	},
									        	"No": function() {
									          		$( this ).dialog( "close" );
									        	}
									      	}
									    });
										
									},
									dataType:'json',
									async:false
								});
							};
						</script>
					</c:if>
					<c:if test="${taggingAuth.isRestorable}">
						<a class="button green mt ml" onclick="restoreTagging()"><small class="icon plus"></small><span>Restore</span></a>
						<script type="text/javascript" >
							function restoreTagging(){
								$('#frm_tagging').attr('action', '<c:url value="/transfer/tagging/list" />' + '<c:out value="/${repositoryKey}/restore"/>');
								$('#frm_tagging').submit();
							};
							
							$(function() {
								$('#frm_tagging').bind('submit',function(){
									if( !isRestoreConfirm ) confirmRestore();
									return isRestoreConfirm;
								});
						   	});
							
							var isRestoreConfirm = false;
							function confirmRestore(){
								$('#div_restoreConfirmMessage .tagName').text('<c:out value="${tagging.tagName}"/>');
								$('#div_restoreConfirmMessage .taggingSeq').text('<c:out value="tagging-${tagging.taggingSeq}"/>');
								$('#div_restoreConfirmMessage .taggingUserId').text('<c:out value="${tagging.taggingUser.userName}(${tagging.taggingUser.userId})"/>');
								$('#div_restoreConfirmMessage .taggingDate').text(haksvn.date.convertToComplexFullFormat(new Date(Number('<c:out value="${tagging.taggingDate}"/>'))));
								$( "#div_restoreConfirmMessage" ).dialog({
							      	modal: true,
							      	width: 450,
							      	title:'Restore Tag To Production',
							      	resizable:false,
							      	buttons: {
							      		"Yes": function() {
							          		$( this ).dialog( "close" );
							          		isRestoreConfirm = true;
							          		haksvn.block.on();
							          		$('#frm_tagging').submit();
							        	},
							        	"No": function() {
							          		$( this ).dialog( "close" );
							        	}
							      	}
							    });
							};
						</script>
					</c:if>
					<a class="button yellow mt ml" onclick="history.back()"><small class="icon play"></small><span>Back to List</span></a>
				</p>
			</form:form>
		</div>
	</div>
	<div class="clear"></div>
</div>

<div id="div_duplicateMessage" style="display:none;">
  	<section>
  		<p>Click "Yes" to overwrite existing Tag</p>
	  	<ul>
	  		<li>Tag name: <b class="tagName"></b></li>
	  		<li>Tagging seq: <b class="taggingSeq"></b></li>
	  		<li>Tagging user: <b class="taggingUserId"></b></li>
	  		<li>Tagging date: <b class="taggingDate"></b></li>
	  	</ul>
  	</section>
</div>

<div id="div_restoreConfirmMessage" style="display:none;">
  	<section>
  		<p>Click "Yes" to restore this tag to "Production" branch</p>
	  	<ul>
	  		<li>Tag name: <b class="tagName"></b></li>
	  		<li>Tagging seq: <b class="taggingSeq"></b></li>
	  		<li>Tagging user: <b class="taggingUserId"></b></li>
	  		<li>Tagging date: <b class="taggingDate"></b></li>
	  	</ul>
  	</section>
</div>
