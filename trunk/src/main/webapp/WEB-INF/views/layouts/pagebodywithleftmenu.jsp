<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<body>
	<tiles:insertAttribute name="header" />
	<div id="wrapper">
		<div id="minwidth">
			<div id="holder">
				<tiles:insertAttribute name="menu" />
				<div id="desc">
					<div class="body">
						<div style="float:left;margin-right:-200px;">
							<tiles:insertAttribute name="leftmenu" />
						</div>
						<div style="float:left; width:100%;">
							<div style="margin-left:200px">
								<tiles:insertAttribute name="content" />
							</div>
						</div>
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
