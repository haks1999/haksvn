<%@ include file="/WEB-INF/views/common/include/taglib.jspf"%>
<style>
  .ui-autocomplete-loading {
    background: white url('/haksvn/resources/images/ui-anim_basic_16x16.gif') right center no-repeat;
  }
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
		var repositorySeq = $("#sel_repository > option:selected").val();
		$.post("<c:url value="/configuration/repositories/listUser/delUser"/>" + "/" + repositorySeq,
				$('#tbl_userList input[name="userId"]:checked').serialize(),
	            function(data){
					$().Message({type:data.type,text:data.text});
					retrieveRepositoryUserList();
					
	        },"json");
	};
	
	function addRepositoryUser(){
		var repositorySeq = $("#sel_repository > option:selected").val();
		var userIds = {userId: selectedUsers, overwrite: $('#ckb_overwrite').is(':checked')};
		$.post("<c:url value="/configuration/repositories/listUser/addUser"/>" + "/" + repositorySeq,
				$.param(userIds,true),
	            function(data){
					$().Message({type:data.type,text:data.text});
					retrieveRepositoryUserList();
					
	        },"json");
	};
	
	function autocompleteSplit( val ) {
		return val.split( /,\s*/ );
	};
	    
	function autocompleteExtractLast( term ) {
		return autocompleteSplit( term ).pop();
	};
	
	var selectedUsers = [];
	function enableSearchUserAutocomplete(){
		$( "#ipt_users" ).autocomplete({
			source: function( request, response ) {
						if( autocompleteExtractLast(request.term).trim().length < 2 ) return;
		          		$.postJSON( "<c:url value="/common/users/find/"/>",
		          					{searchString: autocompleteExtractLast(request.term)}, 
		          					function(data){
			            				if (!data.length || data.length < 1) {
			            					data = [{ noresult: true, noresultmsg: "No Result!"}];
			            				}
			            				response(data);
		        	  	});
		          
		        	},
			minLength: 2,
			focus: 	function( event, ui ) {
						// prevent value inserted on focus
						return false;
		      		},
			select: function( event, ui ) {
						
						var terms = autocompleteSplit( this.value );
				        terms.pop();
				        if (ui.item.noresult || selectedUsers.join( "," ).indexOf(ui.item.value) > -1){
				        	 //terms.push( "" );
						}else{
							selectedUsers.push(ui.item.value);
							terms.push( ui.item.value );
						}
				        terms.push( "" );
				        this.value = terms.join( ", " );
				        return false;
					},
			open: 	function() {
						$( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
					},
			close: 	function() {
						$( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
					}
		})
		.data( "autocomplete" )._renderItem = function( ul, item ) {
			if( item.noresult ){
				return $( "<li>" )
		        .append( "<a class=\"italic\">" + item.noresultmsg + "</a>" )
		        .appendTo( ul ); 
			}else{
				return $( "<li>" )
		        .append( "<a>" + item.label + "</a>" )
		        .appendTo( ul );
			}
		     
		};
	};
	
	function disableSearchUserAutocomplete(){
		selectedUsers = [];
		//$("#ipt_users").autocomplete( "destroy" );
		$('#ipt_users').data().autocomplete.term = null;
		$('#ipt_users').val('');
	};
	
	function openSearchUserDialog(){
		//$('#AutocompleteElementID').data().autocomplete.term = null;
		//selectedUsers = [];
		
		enableSearchUserAutocomplete();
		$("#div_searchUser").dialog({
			resizable: false,
			height: 260,
		    modal: true,
		    buttons: {
		          "Confirm": function() {
		        	  addRepositoryUser();
		        	  disableSearchUserAutocomplete();
		            $( this ).dialog( "close" );
		          },
		          Cancel: function() {
		        	  disableSearchUserAutocomplete();
		            $( this ).dialog( "close" );
		          }
		        }
	    });
	};
	
	
	
</script>
<div id="table" class="help">
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
						<th class="checkbox"><input type="checkbox"/></th>
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
							<a href="<c:url value="/configuration/users/list/"/>">test</a>
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
  		<p>Enter user id or user name <br>(minimum 3 characters)</p>
		<input id="ipt_users" class="text w_20">
		<span class="italic"><input id="ckb_overwrite" type="checkbox"/>Overwrite if a user exists in passwd</span>
	</div>
</div>
