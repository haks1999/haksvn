<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<style type="text/css">
.chzn-results {height: 100px;}
</style>
<script type="text/javascript">
	$(function() {
		retrieveRepositoryUserList();
		$("#sel_repository").change(retrieveRepositoryUserList);
		
	});
	    
	function retrieveRepositoryUserList(){
		var repositorySeq = $("#sel_repository > option:selected").val();
		$("#tbl_userList tbody tr:not(.sample)").remove();
		$.getJSON( "<c:url value="/configuration/repositories/listUser"/>" + "/" + repositorySeq
				,function(data) {
					for( var inx = 0 ; inx < data.length ; inx++ ){
						var row = $("#tbl_userList > tbody > .sample").clone();
						var defaultLink = $(row).find(".userId a").attr("href");
						$(row).find(".userId a").text(data[inx].userId).attr("href", defaultLink + data[inx].userSeq);
						$(row).find("input[name='userId']").val(data[inx].userId);
						$(row).children(".userName").text(data[inx].userName);
						$(row).children(".email").text(data[inx].email);
						$(row).children(".authType").text(data[inx].authTypeCode.codeName);
						$(row).removeClass("sample");
						$('#tbl_userList > tbody').append(row);
					}
		});
	};
	
	function delRepositoryUser(){
		if($('#tbl_userList input[name="userId"]:checked').length < 1 ) return; 
		var repositorySeq = $("#sel_repository > option:selected").val();
		$.post("<c:url value="/configuration/repositories/listUser/delUser"/>" + "/" + repositorySeq,
				$('#tbl_userList input[name="userId"]:checked').serialize(),
	            function(data){
					$().Message({type:data.type,text:data.text});
					retrieveRepositoryUserList();
					
	        },"json");
	};
	
	function addRepositoryUser(){
		var selectedUsers = $("#div_searchUser .chzn-select").chosen().val();
		if(selectedUsers.length < 1) return;
		var repositorySeq = $("#sel_repository > option:selected").val();
		var userIds = {userId: selectedUsers, overwrite: $('#ckb_overwrite').is(':checked')};
		$.post("<c:url value="/configuration/repositories/listUser/addUser"/>" + "/" + repositorySeq,
				$.param(userIds,true),
	            function(data){
					$().Message({type:data.type,text:data.text});
					retrieveRepositoryUserList();
					
	        },"json");
	};
	
	function openSearchUserDialog(){
		
		$("#div_searchUser .chzn-select").val('').trigger("liszt:updated");
		$.getJSON( "<c:url value="/common/users/find/"/>",
				{searchString: ""}, 
				function(data){
					$("#div_searchUser .chzn-select option").remove();
					for( var inx = 0 ; inx < data.length ; inx++ ){
						$("#div_searchUser .chzn-select").append("<option value=\"" +  data[inx].userId + "\">" + data[inx].userName + "(" + data[inx].userName + ")" + "</option>");
					}
					$("#div_searchUser .chzn-select").chosen({width:"100%"});
		    	}
		);
		
		$("#div_searchUser").dialog({
			resizable: false,
			height: 300,
			width: 450,
		    modal: true,
		    buttons: {
		          "Confirm": function() {
		        	  addRepositoryUser();
		              $( this ).dialog( "close" );
		          },
		          Cancel: function() {
		              $( this ).dialog( "close" );
		          }
		        }
	    });
	};
	
	
	
</script>
<div class="content-page">
	<h1></h1>
	<div class="col w10">
		<div class="content">
			<div class="box">
				<div class="head"><div></div></div>
				<div class="desc">
					<p>
						<label>Repository Name</label> 
						<select id="sel_repository">
							<c:forEach items="${repositoryList}" var="repository">
								<option value="<c:out value="${repository.repositorySeq}"/>">
									<c:out value="${repository.repositoryName}" />
								</option>
							</c:forEach>
						</select>
					</p>
				</div>
				<div class="bottom"><div></div></div>
			</div>

			<table id="tbl_userList">
				<thead>
					<tr>
						<th class="checkbox"></th>
						<th>ID</th>
						<th>Name</th>
						<th>Email</th>
						<th>User Authority</th>
					</tr>
				</thead>
				<tbody>
					<tr class="sample">
						<td class="checkbox"><input name="userId" type="checkbox"/></td>
						<td class="userId">
							<font class="path"><a href="<c:url value="/configuration/users/list/"/>"></a></font>
						</td>
						<td class="userName"></td>
						<td class="email"></td>
						<td class="authType"></td>
					</tr>
				</tbody>
			</table>
			
			<p>
				<a class="button green mt ml" onclick="openSearchUserDialog()"><small class="icon plus"></small><span>Add User</span></a>
				<a class="button red mt ml" onclick="delRepositoryUser()"><small class="icon cross"></small><span>Delete User</span></a>
			</p>
		</div>
	</div>
	<div class="clear"></div>
</div>


<div id="div_searchUser" title="Search User" style="display:none;">
	<div class="module text">
		
		<select data-placeholder="Find Users" class="chzn-select" multiple>
        </select>
          
		<span class="italic"><input id="ckb_overwrite" type="checkbox"/>Overwrite if a user exists in passwd</span>
	</div>
</div>
