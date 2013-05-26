<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<script type="text/javascript">
	
	$(function() {
		$('#frm_tagging').attr('action', '<c:url value="/transfer/tagging/list/${repositorySeq}/save" />');
		transformDateField();
   	});
	
	function transformDateField(){
		var taggingDate = Number('<c:out value="${tagging.taggingDate}"/>');
		if( taggingDate > 0 ) $('#frm_tagging input.taggingDate').val(haksvn.date.convertToComplexFullFormat(new Date(taggingDate)));
	};
	
	
	
</script>
<c:set var="repoBrowsePathLink" value="${pageContext.request.contextPath}/source/browse/${repositorySeq}"/>
<div id="table" class="help">
	<h1>Tagging Information</h1>
	<div class="col w10 last">
		<div class="content">
		
			<form:form commandName="tagging" class="w200" id="frm_tagging" method="post" onsubmit="javascript:haksvn.block.on();">
				<p><span class="strong">Detail</span></p>
				<p>
					<form:label path="taggingSeq" class="left">Tagging Seq</form:label>
					<form:input class="text w_20 readOnly ${tagging.taggingSeq < 1?'visible-hidden':''}" path="taggingSeq" readonly="true"/>
				</p>
				<p>
					<form:label path="repositorySeq" class="left">Repository</form:label>
					<form:select path="repositorySeq" disabled="true" items="${repositoryList}" itemValue="repositorySeq" itemLabel="repositoryName"/>
				</p>
				<p>
					<form:label path="taggingTypeCode.codeId" class="left">Type</form:label>
					<form:select path="taggingTypeCode.codeId" disabled="true" items="${requestScope['tagging.type.code']}" itemValue="codeId" itemLabel="codeName"/>
				</p>
				<p>
					<form:label path="srcPath" class="left">Source Path</form:label>
					<form:hidden class="text w_20" disabled="true" path="srcPath"/>
					<font class="path"><a href="${repoBrowsePathLink}${tagging.srcPath}"><c:out value="${tagging.srcPath}"/></a></font><input type="text" class="text visible-hidden"/>
				</p>
				<p>
					<form:label path="destPath" class="left">Destination Path</form:label>
					<form:hidden class="text w_20" disabled="true" path="destPath"/>
					<font class="path"><a href="${repoBrowsePathLink}${tagging.destPath}"><c:out value="${tagging.destPath}"/></a></font><input type="text" class="text visible-hidden"/>
				</p>
				<p>
					<form:label path="tagName" class="left">Tag Name</form:label>
					<form:input class="text w_20" disabled="${not taggingAuth.isCreatable}" path="tagName"/>
					<form:errors path="tagName" />
				</p>
				<p>
					<form:label path="description" class="left">Description</form:label>
					<form:textarea class="text" disabled="${not taggingAuth.isCreatable}" cols="50" rows="5" path="description"/>
					<form:errors path="description" />
				</p>
				<p>
					<form:hidden path="taggingUser.userId" />
					<form:hidden path="taggingUser.userName" />
					<label class="left">Tagging User</label>
					<input type="text" class="text w_30 readOnly" readonly value="${tagging.taggingUser.userName}(${tagging.taggingUser.userId})"/>
				</p>
				<p>
					<form:hidden path="taggingDate" />
					<label class="left">Tagging Date</label>
					<input type="text" class="text w_30 readOnly taggingDate" readonly/>
				</p>
				<p>
					<label class="left"></label>
					<c:if test="${taggingAuth.isCreatable}">
						<a class="button green mt ml" onclick="createTagging()"><small class="icon plus"></small><span>Create</span></a>
						<script type="text/javascript" >
							function createTagging(){
								$('#frm_tagging').attr('action', '<c:url value="/transfer/tagging/list" />' + '<c:out value="/${repositorySeq}/create"/>');
								$('#frm_tagging').submit();
							};
						</script>
					</c:if>
					<c:if test="${taggingAuth.isRestorable}">
						<a class="button green mt ml" onclick="restoreTagging()"><small class="icon plus"></small><span>Restore</span></a>
						<script type="text/javascript" >
							function restoreTagging(){
								$('#frm_tagging').attr('action', '<c:url value="/transfer/tagging/list" />' + '<c:out value="/${repositorySeq}/restore"/>');
								$('#frm_tagging').submit();
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

