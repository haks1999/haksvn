<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title><tiles:getAsString name="title"/></title>
		<link rel="stylesheet" href="css/style.css" type="text/css" media="screen" charset="utf-8" />
		<script src="js/jquery.js" type="text/javascript" charset="utf-8"></script>
		<script src="js/global.js" type="text/javascript" charset="utf-8"></script>
		<script src="js/modal.js" type="text/javascript" charset="utf-8"></script>
	</head>
	<body>
		<tiles:insertAttribute name="header" />
		<div id="wrapper">
			<div id="minwidth">
				<div id="holder">
					<tiles:insertAttribute name="menu" />
					<div id="desc">
						<div class="body">
							<div id="messages"></div>
							<div class="clear"></div>
							
							
							
							
							
							
							
							
							<div id="message" class="help">
							<h1>Message:</h1>
							<div class="col w10 last">
								<div class="content">
									<div class="success">
										<div class="tl"></div><div class="tr"></div>
										<div class="desc">
											<p>Its Success, js call: $().Message({type:'success'});</p>
										</div>
										<div class="bl"></div><div class="br"></div>
									</div>
									<div class="error">
										<div class="tl"></div><div class="tr"></div>
										<div class="desc">
											<p>Something wrong in here, js call: $().Message({type:'error'});</p>
										</div>
										<div class="bl"></div><div class="br"></div>
									</div>
									<div class="notice">
										<div class="tl"></div><div class="tr"></div>
										<div class="desc">
											<p>Its just a notice, js call: $().Message({type:'notice'});</p>
										</div>
										<div class="bl"></div><div class="br"></div>
									</div>
									<p>
										Avaible options:<br />
										$().Message({type:'error',time:2000,text:"Some text",target:"#targetdiv",click:true});
									</p>
									<ul>
										<li><span class="strong">type:</span> you can choose form <span class="highlight">success</span>, <span class="highlight">error</span> and <span class="highlight">notice</span>(or anything else, this is the classname, so you can create your own message style in css)</li>
										<li><span class="strong">time:</span> number of milliseconds visible the message</li>
										<li><span class="strong">text:</span> the text value to set the contents of the element to.</li>
										<li><span class="strong">target:</span> the target div.</li>
										<li><span class="strong">click:</span> if you click on the message, it will slide away, if you don't want that for some reason, just add click:false.</li>
									</ul>
								</div>
							</div>
							<div class="clear"></div>
							</div>
							
							
							
							<div id="table" class="help">
							<h1>Table:</h1>
							<div class="col w10 last">
								<div class="content">
									<table>
										<tr>
											<th class="checkbox"><input type="checkbox" name="checkbox" /></th>
											<th>Th #1</th>
											<th>Th #2</th>
											<th>Th #3</th>
											<th>Th #4</th>
											<th>Th #5</th>
											<th>Th #6</th>
										</tr>
										<tr id="id_1">
											<td class="checkbox"><input type="checkbox" name="checkbox" /></td>
											<td>Lorem</td>
											<td>Ipsum</td>
											<td>Dolor</td>
											<td>Sit</td>
											<td>Amez</td>
											<td>Consectetur</td>
										</tr>
										<tr id="id_2">
											<td class="checkbox"><input type="checkbox" name="checkbox" /></td>
											<td>Lorem</td>
											<td>Ipsum</td>
											<td>Dolor</td>
											<td>Sit</td>
											<td>Amez</td>
											<td>Consectetur</td>
										</tr>
										<tr id="id_3">
											<td class="checkbox"><input type="checkbox" name="checkbox" /></td>
											<td>Lorem</td>
											<td>Ipsum</td>
											<td>Dolor</td>
											<td>Sit</td>
											<td>Amez</td>
											<td>Consectetur</td>
										</tr>
										<tr id="id_4">
											<td class="checkbox"><input type="checkbox" name="checkbox" /></td>
											<td>Lorem</td>
											<td>Ipsum</td>
											<td>Dolor</td>
											<td>Sit</td>
											<td>Amez</td>
											<td>Consectetur</td>
										</tr>
										<tr id="id_5">
											<td class="checkbox"><input type="checkbox" name="checkbox" /></td>
											<td>Lorem</td>
											<td>Ipsum</td>
											<td>Dolor</td>
											<td>Sit</td>
											<td>Amez</td>
											<td>Consectetur</td>
										</tr>
										<tr id="id_6">
											<td class="checkbox"><input type="checkbox" name="checkbox" /></td>
											<td>Lorem</td>
											<td>Ipsum</td>
											<td>Dolor</td>
											<td>Sit</td>
											<td>Amez</td>
											<td>Consectetur</td>
										</tr>
										<tr id="id_7">
											<td class="checkbox"><input type="checkbox" name="checkbox" /></td>
											<td>Lorem</td>
											<td>Ipsum</td>
											<td>Dolor</td>
											<td>Sit</td>
											<td>Amez</td>
											<td>Consectetur</td>
										</tr>
										<tr id="id_8">
											<td class="checkbox"><input type="checkbox" name="checkbox" /></td>
											<td>Lorem</td>
											<td>Ipsum</td>
											<td>Dolor</td>
											<td>Sit</td>
											<td>Amez</td>
											<td>Consectetur</td>
										</tr>
										<tr id="id_9">
											<td class="checkbox"><input type="checkbox" name="checkbox" /></td>
											<td>Lorem</td>
											<td>Ipsum</td>
											<td>Dolor</td>
											<td>Sit</td>
											<td>Amez</td>
											<td>Consectetur</td>
										</tr>
										<tr id="id_10">
											<td class="checkbox"><input type="checkbox" name="checkbox" /></td>
											<td>Lorem</td>
											<td>Ipsum</td>
											<td>Dolor</td>
											<td>Sit</td>
											<td>Amez</td>
											<td>Consectetur</td>
										</tr>
									</table>
								</div>							
							</div>
							<div class="clear"></div>
							</div>
							
							<div class="col w10 last">
								<div class="content">
									<p>The end...</p>
								</div>
							</div>
							<div class="clear"></div>
						</div>
						<div class="clear"></div>
						<div id="body_footer">
							<div id="bottom_left"><div id="bottom_right"></div></div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<tiles:insertAttribute name="footer" />
	</body>
</html>