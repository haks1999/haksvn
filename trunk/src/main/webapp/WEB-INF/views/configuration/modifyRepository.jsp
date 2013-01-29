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
		
			<form class="w200" name="repositoryForm" method="post" action="<c:url value="/configuration/repositories/add"/>">
				<p>
					<label for="repositoryLocation" class="left">Repository Location</label>
					<input type="text" class="text w_30" name="repositoryLocation" value="<c:out value="${repository.repositoryLocation}" />"/>
				</p>
				<p>
					<label for="repositoryName" class="left">Repository Name</label>
					<input type="text" class="text w_20" name="repositoryName" value="<c:out value="${repository.repositoryName}" />"/>
				</p>
				<p>
					<label for="select" class="left">Repository Status</label>
					
					<select name="repositoryStatus">
						<option value="10">active</option>
						<option value="20">inactive</option>
					</select>
					<haksvn:combo message="test"></haksvn:combo>

				</p>
				<p>
					<label class="left"></label>
					<a class="button green mt ml form_submit"><small class="icon check"></small><span>Confirm</span></a>
					<a class="button red mt ml"><small class="icon cross"></small><span>Cancel</span></a>
				</p>
			</form>
						
		</div>
	</div>
	<div class="clear"></div>
</div>