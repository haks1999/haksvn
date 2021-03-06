<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<body>
	<tiles:insertAttribute name="header" />
	<div id="_global_block"></div>
	<div id="wrapper">
		<div id="minwidth">
			<div id="holder">
				<tiles:insertAttribute name="menu" />
				<div id="desc">
					<div class="body">
						<div id="_global_message"></div>
						<tiles:insertAttribute name="content" />
						<!--
						<div id="messages">mess</div>
						<div class="clear">clear</div>
						<div class="col w10 last">
							<div class="content">
								<p>The end...</p>
							</div>
						</div>
						<div class="clear"></div>
						-->
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
