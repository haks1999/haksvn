<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<style type="text/css">
#menu ul li a:hover{background-image:none;}
</style>
<script type="text/javascript">
$(function() {
	
	$('#frm_login input[name$="userId"]').each(function(){ this.focus();});
	$("#frm_login input").keypress(function(event) {
	    if (event.which == 13) {
	        event.preventDefault();
	        $("#frm_login").submit();
	    }
	});
	
	var resultMessage = '<c:out value="${resultMessage}"/>';
	if( resultMessage.length > 0 ){
		$().Message({type:'error', text: resultMessage});
	}
});
</script>
<body style="overflow:hidden;">
	<tiles:insertAttribute name="header" />
	<div id="wrapper" style="position:relative;margin-left:50%;left:-230px;width:460px;margin-top:50px;min-width:200px;">
		<div id="minwidth">
			<div id="holder">
				<div id="menu">
					<div id="left"></div>
					<div id="right"></div>
					<ul>
						<li><a><span>Haksvn Login</span></a></li>
					</ul>
					<div class="clear"></div>
				</div>
				<div id="desc">
					<div class="body">
						<div id="_global_message"></div>
						<form id="frm_login" method="post" class="w100" action="<c:url value="/login"/>" style="margin-top:20px;padding-bottom:20px;">
							<p style="text-align:center;padding-bottom:10px;">Simple Subversion Repository Management</p>
							<p>
								<label class="left">User ID</label>
								<input type="text" class="text w_230" name="userId"/>
							</p>
							<p>
								<label class="left">Password</label>
								<input type="password" class="text w_230" name="userPasswd"/>
							</p>
							<p>
								<label class="left"></label>
								<a class="button green mt form_submit"><small class="icon single_user"></small><span>LOG IN</span></a>
							</p>
						</form>
					</div>
					<div class="clear"></div>
					<div id="body_footer">
						<div id="bottom_left">
							<div id="bottom_right"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<tiles:insertAttribute name="footer" />
</body>
