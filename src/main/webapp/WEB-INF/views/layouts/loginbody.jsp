<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
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
<body>
	<tiles:insertAttribute name="header" />
	<div id="wrapper">
		<div id="minwidth">
			<div id="holder">
				<div id="menu">
					<div id="left"></div>
					<div id="right"></div>
					<ul>
						<li><a href="#"><span>Login</span></a></li>
					</ul>
					<div class="clear"></div>
				</div>
				<div id="desc">
					<div class="body">
						<div id="_global_message"></div>
						<form id="frm_login" method="post" class="w100" action="<c:url value="/login"/>" style="margin-top:20px;padding-bottom:20px;">
							<p>
								<label class="left">User ID</label>
								<input type="text" class="text w_20" name="userId"/>
							</p>
							<p>
								<label class="left">Password</label>
								<input type="password" class="text w_20" name="userPasswd"/>
							</p>
							<p>
								<label class="left"></label>
								<a class="button green mt ml form_submit"><span>Login</span></a>
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
