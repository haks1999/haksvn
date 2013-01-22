<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
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
						<form name="loginForm" method="post" action="<c:url value="/login"/>">
							<p>
								<span class="strong">username</span>
								<label></label>
								<input type="text" class="text" name="username"/>
								<br />
							</p>
							<p>
								<span class="strong">password</span>
								<label></label>
								<input type="password" class="text" name="password"/>
								<br />
							</p>
							<a class="button green" id="doLogin" onclick="document.loginForm.submit()"><span>Login</span></a>
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
