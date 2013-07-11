<%@ page pageEncoding="UTF-8" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/tlds/haksvn.tld" prefix="haksvn" %>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="EXPIRES" content="-1">
	<meta http-equiv="Pragma" content="no-cache" />
	<title><tiles:getAsString name="title" /></title>
	
	<link rel="shortcut icon" href="<c:url value="/images/haksvn.ico"/>" />
	<link rel="stylesheet" href="<c:url value="/css/style.css"/>" type="text/css"	media="screen" />
	<link rel="stylesheet" href="<c:url value="/css/haksvn.css"/>" type="text/css"	media="screen" />
	<link rel="stylesheet" href="<c:url value="/css/jquery-ui-1.9.2.custom.css"/>" type="text/css"	media="screen" />
	<link rel="stylesheet" href="<c:url value="/css/ui.dynatree.css"/>" type="text/css"	media="screen" />
	<link rel="stylesheet" href="<c:url value="/css/chosen.css"/>" type="text/css"	media="screen" />
	<script src="<c:url value="/js/jquery-1.8.3.js"/>" type="text/javascript" charset="utf-8"></script>
	<script src="<c:url value="/js/jquery.blockUI.js"/>" type="text/javascript" charset="utf-8"></script>
	<script src="<c:url value="/js/jquery.form.js"/>" type="text/javascript" charset="utf-8"></script>
	<script src="<c:url value="/js/jquery.cookie.js"/>" type="text/javascript" charset="utf-8"></script>
	<script src="<c:url value="/js/jquery.validate.min.js"/>" type="text/javascript" charset="utf-8"></script>
	<script src="<c:url value="/js/jquery-ui-1.9.2.custom.js"/>" type="text/javascript" charset="utf-8"></script>
	<script src="<c:url value="/js/jquery.dynatree-1.2.4.js"/>" type="text/javascript" charset="utf-8"></script>
	<script src="<c:url value="/js/chosen.jquery.min.js"/>" type="text/javascript" charset="utf-8"></script>
	<script src="<c:url value="/js/global.js"/>" type="text/javascript" charset="utf-8"></script>
	<script src="<c:url value="/js/modal.js"/>" type="text/javascript" charset="utf-8"></script>
	<script src="<c:url value="/js/haksvn.js"/>" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript">
		$.ajaxSetup({ 
			cache : false,
			error : function(jqXHR, textStatus, errorThrown) {
				var errorMsg = jqXHR.responseText !== '' ? jqXHR.responseText:errorThrown;
				$().Message({type:'error', text: textStatus + " : " + jqXHR.status + " : " + errorMsg});
				haksvn.block.off();
		    },
		    complete : function( jqXHR ,textStatus ){
		    	//$.unblockUI();
		    	//$('#loader-main').hide();
		    },
		    beforeSend: function(){
		    	//$('#loader-main').show();
		    }
		});
		
		$.validator.setDefaults({
			errorPlacement: function(error, element) {
				error.appendTo( element.parent().find(".form-status") );
			},
			errorClass: 'invalid'
		});
		
		$.validator.addMethod("svnpath", function(value, element) {
		     return /^\/.+[^\/]$/.test(value);
		}, "<spring:message code="validation.svnpath" />");
		
		$.validator.addMethod("minSelect", function(value, element, params) {
		     return value >= Number(params);
		}, "<spring:message code="validation.minSelect" />");
		
		$.validator.addMethod("alphabet", function(value, element, params) {
		     return /^[a-zA-Z]+$/.test(value);
		}, "<spring:message code="validation.alphabet" />");
		
		$.extend($.validator.messages, {
		    date: "<spring:message code="validation.date" />",
		    digits: "<spring:message code="validation.digits" />",
		    email: "<spring:message code="validation.email" />",
		    equalTo: "<spring:message code="validation.equalTo" />",
		    max: "<spring:message code="validation.max" />",
			maxlength: "<spring:message code="validation.maxlength" />",
			minlength: "<spring:message code="validation.minlength" />",
		    min: "<spring:message code="validation.min" />",
		    number: "<spring:message code="validation.number" />",
		    range: "<spring:message code="validation.range" />",
		    rangelength: "<spring:message code="validation.rangelength" />",
		    remote: "<spring:message code="validation.remote" />",
			required: "<spring:message code="validation.required" />",
		    url: "<spring:message code="validation.url" />"
		});
		
		function _setLinkNewWindow(){
			$("font.path.open-window").each(function(){
				$(this).after('<a class="open-window" target="_blank" href="' + $(this).find('a').attr('href') + '"><img src="<c:url value="/images/open-window.gif"/>" title="Open New Window"/></a>');
			});
		};
		
		function _setFormHelper(){
			$("form span.form-help").each(function(){
				var messageBody = $(this).text();
				$(this).empty();
				$(this).append("<label class=\"help\"></label>");
				$(this).parent().after("<div class=\"form-help info\"><div class=\"tl\"></div><div class=\"tr\"></div>"
						+ "<div class=\"desc\"><p></p></div><div class=\"bl\"></div><div class=\"br\"></div></div>");
				$(this).parent().next("div.form-help").find("p").html(messageBody);
				$(this).find("label.help").click(function(){
					$(this).parent().parent().next("div.form-help").toggle();
				});
				$(this).show();
			});
		};
		
		$(function() {
			// IE -- http://stackoverflow.com/questions/7109120/add-blank-option-to-top-of-select-and-make-it-the-selected-option-in-ie
			$('select.all').prepend("<option value=''>All</option>").val('');
			_setLinkNewWindow();
			_setFormHelper();
		});
	</script>
</head>